package fr.epf.moov

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
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
        val type = intent.getStringExtra("type")
        val directions = intent.getStringExtra("directions")
        var resources: Resources = this.resources
        val service = retrofit().create(RATPService::class.java)

        directions_textview.text = directions

        if(type == "metros") {
            type_imageview.setImageResource(R.drawable.metro)
            val drawableName: String = "m${code}"
            val id: Int =
                resources.getIdentifier(drawableName, "drawable", this.packageName)
            pictogram_imageview.setImageResource(id)
        }
        if(type == "rers") {
            type_imageview.setImageResource(R.drawable.rer)
            val newCode = code.toLowerCase()
            val drawableName : String = "m${newCode}"
            val id: Int =
                resources.getIdentifier(drawableName, "drawable", this.packageName)
            pictogram_imageview.setImageResource(id)
        }
        if(type == "tramways") {
            type_imageview.setImageResource(R.drawable.tramway)
            val drawableName : String = "t${code}"
            val id: Int =
                resources.getIdentifier(drawableName, "drawable", this.packageName)
            pictogram_imageview.setImageResource(id)
        }

        runBlocking {
            val result = service.getTrafficLine(type, code)
            message_textview.text = result.result.message
            if(result.result.slug == "normal") {
                trafic_card.visibility = View.GONE
            } else logo_warning_imageview.setImageResource(R.drawable.attention)
        }


        runBlocking {
            val result = service.listStationsMetros(type, code)
            result.result.stations?.map {
                val station = Station(
                    0,
                    type,
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