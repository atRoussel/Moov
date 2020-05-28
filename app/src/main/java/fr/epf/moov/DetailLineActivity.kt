package fr.epf.moov

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import fr.epf.moov.adapter.StationAdapter
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

        val drawableName: String = "m${code}"

        var resources: Resources = this.resources
        val id: Int =
            resources.getIdentifier(drawableName, "drawable", this.packageName)
        pictogram_imageview.setImageResource(id)

        runBlocking {
            val result = service.getTrafficLine("metros", code)
            message_textview.text = result.result.message
            if(result.result.slug == "normal") {
                trafic_card.visibility = View.GONE
            } else logo_warning_imageview.setImageResource(R.drawable.attention)
        }


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

        station_recyclerview.adapter =
            StationAdapter(ListStations)

    }
}