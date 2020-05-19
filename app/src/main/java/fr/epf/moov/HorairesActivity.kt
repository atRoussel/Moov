package fr.epf.moov

import android.app.Activity
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.bumptech.glide.Glide
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import fr.epf.moov.data.AppDatabase
import fr.epf.moov.data.StationDao
import fr.epf.moov.model.Station
import fr.epf.moov.service.RATPService
import kotlinx.android.synthetic.main.activity_horaires.*
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import java.net.URL


class HorairesActivity : AppCompatActivity() {

    var schedulesList: MutableList<String> = mutableListOf()
    var allStations: List<Station>? = null
    var allStationsName: MutableList<String> = mutableListOf()
    private var stationDao: StationDao? = null
    val service = retrofit().create(RATPService::class.java)
    var listDestinations: List<String>? = null
    var way: String = "A"
    var url: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horaires)

        schedules_recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val database =
            Room.databaseBuilder(this, AppDatabase::class.java, "listStations")
                .build()
        stationDao = database.getStationDao()

        //Récupération de toutes les stations
        runBlocking {
            allStations = stationDao?.getStations()
            allStations?.map {
                allStationsName.add(it.nameStation)
            }
        }


        val adapter: ArrayAdapter<String> =
            ArrayAdapter(this, android.R.layout.simple_list_item_1, allStationsName)
        autoComplete_stations.setAdapter(adapter)


        schedules_button.setOnClickListener {

            val stationName = autoComplete_stations.text.toString()
            var slug = ""
            var type = ""
            var code = ""
            Log.d("OU", "button")
            allStations?.forEach {
                if (it.nameStation == stationName) {
                    slug = it.slugStation
                    type = it.typeLine
                    code = it.codeLine
                    Log.d("Stations", slug)
                }
            }


            runBlocking {
                val result = service.getStation(type, code)
                listDestinations = getListDestinations(result.result.directions)
            }

            runBlocking {
                val result = service.getSchedules(type, code, slug, way)
                schedulesList.clear()


                result.result.schedules.map {
                    var schedule = it.message
                    schedulesList.add(schedule)

                }
            }
            Log.d("OU", schedulesList.toString())

            schedules_recyclerview.adapter = ScheduleAdapter(schedulesList)

            station_name_textview.text = stationName
            aller_textview.text = listDestinations?.get(0)
            retour_textview.text = listDestinations?.get(1)

            val cityCsv = resources.openRawResource(R.raw.pictogrammes)
            val listPictogrammes: List<List<String>> = csvReader().readAll(cityCsv)

            val drawableName : String = "m${code}"
           var resources: Resources = this.resources
            val id: Int =
                resources.getIdentifier(drawableName, "drawable", this.packageName)
            pictogram_imageview.setImageResource(id)


         /* listPictogrammes.map {
                var listp = getListPictogramme(it.toString())
                if (listp[1] == "M${code}") {
                    url = listp[0]
                }
            }

            val policy =
                StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)*/


          /* val imageview: ImageView = findViewById(R.id.pictogram_imageview)
            fetchSvg(this, url!!, pictogram_imageview)*/


        /*Glide.with(this)
                .load(url)
                .centerCrop()
                .into(pictogram_imageview)*/



            global_schedule_layout.visibility = View.VISIBLE


        destinations_exchange.setOnClickListener {
            if (way == "A") {
                way = "R"
                runBlocking {
                    val result = service.getSchedules(type, code, slug, way)

                    schedulesList.clear()

                    result.result.schedules.map {
                        var schedule = it.message
                        schedulesList.add(schedule)

                    }
                }
                schedules_recyclerview.adapter = ScheduleAdapter(schedulesList)
                aller_textview.text = listDestinations?.get(1)
                retour_textview.text = listDestinations?.get(0)
            } else if (way == "R") {
                way = "A"
                runBlocking {
                    val result = service.getSchedules(type, code, slug, way)

                    schedulesList.clear()

                    result.result.schedules.map {
                        var schedule = it.message
                        schedulesList.add(schedule)

                    }
                }
                schedules_recyclerview.adapter = ScheduleAdapter(schedulesList)
                aller_textview.text = listDestinations?.get(0)
                retour_textview.text = listDestinations?.get(1)
            }
        }

    }

    }


    override fun onResume() {
        super.onResume()
    }


    fun getListDestinations(destinations: String): List<String> {
        return destinations.split(" / ")
    }

    fun getListPictogramme(pictogrammes: String): List<String> {
        val stringPictogramme = pictogrammes.substring(1, pictogrammes.length - 1)
        return stringPictogramme.split(";")
    }
}