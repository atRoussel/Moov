package fr.epf.moov

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import fr.epf.moov.model.Station
import kotlinx.android.synthetic.main.activity_detail_line.*
import fr.epf.moov.service.RATPService
import fr.epf.moov.service.retrofit
import kotlinx.coroutines.runBlocking

class DetailLineActivity: AppCompatActivity() {

    var ListStations : MutableList<fr.epf.moov.model.Station> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_line)

        val code = intent.getStringExtra("code")

        val service = retrofit().create(RATPService::class.java)
        runBlocking {
            val result = service.listStationsMetros("metros", "${code}")


            result.result.stations?.map {
                val station = Station(
                    0,
                    "metros",
                    code,
                    it.name,
                    it.slug,
                    null,
                false)
                ListStations.add(station)

            }
        }
        station_recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        station_recyclerview.adapter = StationAdapter(ListStations)

    }
}