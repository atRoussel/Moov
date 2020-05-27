package fr.epf.moov

import android.content.res.Resources
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import fr.epf.moov.adapter.ScheduleAdapter
import fr.epf.moov.data.AppDatabase
import fr.epf.moov.data.StationDao
import fr.epf.moov.model.MetroLine
import fr.epf.moov.model.Station
import fr.epf.moov.service.RATPService
import fr.epf.moov.service.retrofit
import kotlinx.android.synthetic.main.activity_detail_station.aller_textview
import kotlinx.android.synthetic.main.activity_detail_station.pictogram_imageview
import kotlinx.android.synthetic.main.activity_detail_station.retour_textview
import kotlinx.android.synthetic.main.activity_detail_station.schedules_recyclerview
import kotlinx.android.synthetic.main.activity_detail_station.station_name_textview
import kotlinx.android.synthetic.main.activity_detail_station.fav_imageview
import kotlinx.android.synthetic.main.activity_detail_station.destinations_exchange
import kotlinx.coroutines.runBlocking

class DetailStationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_station)

        var schedulesList: MutableList<String> = mutableListOf()
        val service = retrofit().create(RATPService::class.java)
        val code = intent.getStringExtra("code")
        val name = intent.getStringExtra("name")
        val slug = intent.getStringExtra("slug")
        val type = intent.getStringExtra("type")

        var allStations: List<Station>? = null
        lateinit var ma_station: Station
        lateinit var direction: String
        var stationDao: StationDao? = null
        var savedStationDao: StationDao? = null
        var changeDirection = false

        val drawableName : String = "m${code}"
        var resources: Resources = this.resources
        val id: Int =
            resources.getIdentifier(drawableName, "drawable", this.packageName)
        pictogram_imageview.setImageResource(id)

        MetroLine.all.forEach {
            if(it.code == code) {
                var directions_line = it.directions.split(" / ")
                direction = it.directions
                aller_textview.text = directions_line[0]
                retour_textview.text = directions_line[1]
            }
        }

        runBlocking {
            val result = service.getSchedules(type, code, slug, "A")
            schedulesList.clear()

            result.result.schedules.map {
                var schedule = it.message
                schedulesList.add(schedule)

            }
        }
        schedules_recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        schedules_recyclerview.adapter =
            ScheduleAdapter(schedulesList)

        destinations_exchange.setOnClickListener{
            if(changeDirection == true) {
                var aller_text = aller_textview.text
                aller_textview.text = retour_textview.text
                retour_textview.text = aller_text
                runBlocking {
                    val result = service.getSchedules(type, code, slug, "A")
                    schedulesList.clear()

                    result.result.schedules.map {
                        var schedule = it.message
                        schedulesList.add(schedule)

                    }
                }
                schedules_recyclerview.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

                schedules_recyclerview.adapter =
                    ScheduleAdapter(schedulesList)

                changeDirection = false
            } else {
                var aller_text = aller_textview.text
                aller_textview.text = retour_textview.text
                retour_textview.text = aller_text
                runBlocking {
                    val result = service.getSchedules(type, code, slug, "R")
                    schedulesList.clear()

                    result.result.schedules.map {
                        var schedule = it.message
                        schedulesList.add(schedule)

                    }
                }
                schedules_recyclerview.layoutManager =
                    LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

                schedules_recyclerview.adapter =
                    ScheduleAdapter(schedulesList)

                changeDirection = true
            }
        }


        val database =
            Room.databaseBuilder(this, AppDatabase::class.java, "listStations")
                .build()
        stationDao = database.getStationDao()

        //Récupération de toutes les stations
        runBlocking {
            allStations = stationDao?.getStations()
            allStations?.map {
                if(it.nameStation == name && it.codeLine == code) {
                    ma_station = it
                }
            }
        }

        ma_station.directionLine = direction

        if(ma_station.favoris == true) {
            fav_imageview.setImageResource(R.drawable.fav_full)
        } else fav_imageview.setImageResource(R.drawable.fav_empty)

        val databasesaved =
            Room.databaseBuilder(this, AppDatabase::class.java, "savedStations")
                .build()
        savedStationDao = databasesaved.getStationDao()

        fav_imageview.setOnClickListener {
            if (ma_station.favoris == true) {
                ma_station.favoris = false
                fav_imageview.setImageResource(R.drawable.fav_empty)
                runBlocking {
                    savedStationDao?.deleteStation(ma_station.id)
                }
                Toast.makeText(this, "La station a été supprimée des favoris", Toast.LENGTH_SHORT).show()
            } else if (ma_station.favoris == false) {
                ma_station.favoris = true
                fav_imageview.setImageResource(R.drawable.fav_full)
                runBlocking {
                    savedStationDao?.addStation(ma_station)
                }
                Toast.makeText(this, "La station a été ajoutée aux favoris", Toast.LENGTH_SHORT).show()
            }
        }

        station_name_textview.text = name
    }
}