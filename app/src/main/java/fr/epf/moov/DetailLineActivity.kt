package fr.epf.moov

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_detail_line.*
import kotlinx.android.synthetic.main.activity_horaires.*
import fr.epf.moov.service.RATPService
import kotlinx.android.synthetic.main.activity_listes.*
import kotlinx.coroutines.runBlocking

class DetailLineActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_line)

        val code = intent.getStringExtra("code")

        val service = retrofit().create(RATPService::class.java)
        runBlocking {
            val result = service.listStationsMetros("metros", "${code}")
            result.result.stations?.map {
                Station.all.add(Station("${it.name}", "${it.slug}"))
            }
        }
        station_recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        station_recyclerview.adapter = StationAdapter(Station.all)

    }
}