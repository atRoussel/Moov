package fr.epf.moov

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_horaires.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import fr.epf.moov.data.AppDatabase
import fr.epf.moov.data.StationDao
import fr.epf.moov.model.Station
import fr.epf.moov.service.RATPService
import kotlinx.coroutines.runBlocking
import java.io.IOException
import java.io.InputStream
import java.util.*


class HorairesActivity : AppCompatActivity() {

    var schedulesList: MutableList<String> = mutableListOf()
    var allStations: List<Station>? = null
    var allStationsName: MutableList<String> = mutableListOf()
    private var stationDao: StationDao? = null
    val service = retrofit().create(RATPService::class.java)
    var listDestinations: List<String>? = null
    var way : String = "A"


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
            Log.d("CCC", type)
            Log.d("CCC", code)

            runBlocking {
                val result = service.getStation(type, code)
                Log.d("CCC", result.result.directions)
                listDestinations = getListDestinations(result.result.directions)
                Log.d("CCC", listDestinations.toString())
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


            destinations_exchange.setOnClickListener {
                if(way == "A"){
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
                }
                else if (way == "R"){
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
        var listDestination = listOf<String>()
        return destinations.split(" / ")
    }
}