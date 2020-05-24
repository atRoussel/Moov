package fr.epf.moov

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.bumptech.glide.Glide
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import fr.epf.moov.data.AppDatabase
import fr.epf.moov.data.StationDao
import fr.epf.moov.model.Station
import fr.epf.moov.service.RATPService
import fr.epf.moov.service.retrofit
import kotlinx.android.synthetic.main.activity_horaires.*
import kotlinx.coroutines.runBlocking
import java.io.InputStream
import java.net.URL


    class RechercheHorairesActivity : AppCompatActivity() {

        var schedulesList: MutableList<String> = mutableListOf()
        var allStations: List<Station>? = null
        var favStations: List<Station>? = mutableListOf()
        var allStationsName: MutableList<String> = mutableListOf()
        private lateinit var station: Station
        private var stationDao: StationDao? = null
        private var savedStationDao: StationDao? = null
        val service = retrofit().create(RATPService::class.java)
        var listDestinations: List<String>? = null
        var stringDestinations:String = ""
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
            val databasesaved =
                Room.databaseBuilder(this, AppDatabase::class.java, "savedStations")
                    .build()
            savedStationDao = databasesaved.getStationDao()
            //Récupération de toutes les stations
            runBlocking {
                allStations = stationDao?.getStations()
                allStations?.map {
                    allStationsName.add("${it.nameStation} ( Métro ${it.codeLine} )")
                }
            }
            //Récupération des stations favoris
            runBlocking {
                favStations = savedStationDao?.getStations()
            }
            val adapter: ArrayAdapter<String> =
                ArrayAdapter(this, android.R.layout.simple_list_item_1, allStationsName)
            autoComplete_stations.setAdapter(adapter)
            schedules_button.setOnClickListener {
                hideKeyboard()
                val autoComplete_text = autoComplete_stations.text.toString()
                val stationName = autoComplete_text.substringBefore(" (")
                val codeLine = autoComplete_text.substringAfter("( Métro ").substringBefore(" )")
                var slug = ""
                var type = ""
                var code = ""
                var favoris = false
                allStations?.forEach {
                    if (it.nameStation == stationName && it.codeLine == codeLine) {
                        slug = it.slugStation
                        type = it.typeLine
                        code = it.codeLine
                        favoris = getFavoris(stationName, code)
                        runBlocking {
                            val result = service.getStation(type, code)
                            stringDestinations = result.result.directions
                            listDestinations = getListDestinations(stringDestinations)
                        }
                    }
                }
                station = Station(
                    0,
                    type,
                    code,
                    stationName,
                    slug,
                    stringDestinations,
                    favoris
                )
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
                val drawableName: String = "m${code}"
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
                if(station.favoris==true)
                    fav_imageview.setImageResource(R.drawable.fav_full)
                global_schedule_layout.visibility = View.VISIBLE
                fav_imageview.setOnClickListener {
                    if (station.favoris == true) {
                        station.favoris = false
                        fav_imageview.setImageResource(R.drawable.fav_empty)
                        runBlocking {
                            savedStationDao?.deleteStation(station.id)
                        }
                        Toast.makeText(this, "La station a été supprimée des favoris", Toast.LENGTH_SHORT).show()
                    } else if (station.favoris == false) {
                        station.favoris = true
                        fav_imageview.setImageResource(R.drawable.fav_full)
                        runBlocking {
                            savedStationDao?.addStation(station)
                        }
                        Toast.makeText(this, "La station a été ajoutée aux favoris", Toast.LENGTH_SHORT).show()
                    }
                }
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
        fun getListDestinations(destinations: String?): List<String>? {
            if (destinations != null) {
                return destinations.split(" / ")
            }
            return null
        }
        fun getListPictogramme(pictogrammes: String): List<String> {
            val stringPictogramme = pictogrammes.substring(1, pictogrammes.length - 1)
            return stringPictogramme.split(";")
        }
        private fun Activity.hideKeyboard() {
            hideKeyboard(currentFocus ?: View(this))
        }
        private fun Context.hideKeyboard(view: View) {
            val inputMethodManager =
                getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
        }
        fun getFavoris(stationName: String, codeLine: String): Boolean {
            var favoris = false
            favStations?.forEach {
                if (stationName == it.nameStation && codeLine == it.codeLine)
                    favoris = true
            }
            return favoris
        }
    }