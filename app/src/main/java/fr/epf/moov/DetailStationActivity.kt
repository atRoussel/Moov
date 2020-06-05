package fr.epf.moov

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
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
import kotlinx.android.synthetic.main.activity_detail_station.*
import kotlinx.coroutines.runBlocking

class DetailStationActivity : AppCompatActivity() {

    var allStations: List<Station>? = null
    lateinit var ma_station: Station
    lateinit var direction: String
    var stationDao: StationDao? = null
    var savedStationDao: StationDao? = null
    var savedStations: List<Station>? = null
    var changeDirection = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_station)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        var schedulesList: MutableList<String> = mutableListOf()
        val service = retrofit().create(RATPService::class.java)
        val code = intent.getStringExtra("code")
        val name = intent.getStringExtra("name")
        val slug = intent.getStringExtra("slug")
        val type = intent.getStringExtra("type")
        var resources: Resources = this.resources



        if(type == "metros") {
            val drawableName: String = "m${code}"
            val id: Int =
                resources.getIdentifier(drawableName, "drawable", this.packageName)
            pictogram_imageview.setImageResource(id)
        }
        if(type == "rers") {
            val newCode = code.toLowerCase()
            val drawableName : String = "m${newCode}"
            val id: Int =
                resources.getIdentifier(drawableName, "drawable", this.packageName)
            pictogram_imageview.setImageResource(id)
        }
        if(type == "tramways") {
            val drawableName : String = "t${code}"
            val id: Int =
                resources.getIdentifier(drawableName, "drawable", this.packageName)
            pictogram_imageview.setImageResource(id)
        }
        Log.d("infos", code + name + slug + type)

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


        var nameMap = ""
        when(type){
            "metros" -> nameMap = "map_m${code}"
            "rers" -> nameMap = "map_r${code.toLowerCase()}"
            "tramways" -> nameMap = "map_t${code}"
        }

        where_layout.setOnClickListener {
            val intent = Intent(this, ImageActivity::class.java)
            intent.putExtra("map", nameMap)
            this.startActivity(intent)
        }




        val database =
            Room.databaseBuilder(this, AppDatabase::class.java, "listStations")
                .build()
        stationDao = database.getStationDao()

        val databasesaved =
            Room.databaseBuilder(this, AppDatabase::class.java, "savedStations")
                .build()
        savedStationDao = databasesaved.getStationDao()




        //Récupération de toutes les stations
        runBlocking {
            allStations = stationDao?.getStations()
            savedStations = savedStationDao?.getStations()
            allStations?.map {
                if(it.nameStation == name && it.codeLine == code) {
                    ma_station = it
                }
            }
        }
        savedStations?.forEach {
            if (it.nameStation == ma_station.nameStation && it.codeLine == it.codeLine)
                ma_station.favoris = true
        }

        ma_station.directionLine = direction

        if(ma_station.favoris == true) {
            fav_imageview.setImageResource(R.drawable.fav_full)
        } else fav_imageview.setImageResource(R.drawable.fav_empty)

        fav_imageview.setOnClickListener {
            if (ma_station.favoris == true) {
                ma_station.favoris = false
                fav_imageview.setImageResource(R.drawable.fav_empty)
                runBlocking {
                    savedStationDao?.deleteStation(ma_station.codeLine, ma_station.nameStation)
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


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}