package fr.epf.moov

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import fr.epf.moov.service.RATPService
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.JsonAdapter
import fr.epf.moov.service.ModelRatp
import kotlinx.android.synthetic.main.activity_listes.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class ListesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listes)

        val service = retrofit().create(RATPService::class.java)

        runBlocking {
            val result = service.listLinesMetros("metros")
            result.result.metros?.map {
                MetroLine.all.add(MetroLine( "${it.code}","${it.name}", "${it.directions}", it.id))
            }
        }

        metroLines_recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        metroLines_recyclerview.adapter = MetroLineAdapter(MetroLine.all)
    }
}
