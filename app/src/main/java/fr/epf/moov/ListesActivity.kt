package fr.epf.moov

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import fr.epf.moov.service.RATPService
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.JsonAdapter
import fr.epf.moov.service.ModelRatp
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
Log.d("APIresult", result.toString())

            result.result.metros?.map {
                Log.d("APIresult", "OK")
                Log.d("APIresult", "${it.code} / ${it.name}")
            }
        }


    }}
