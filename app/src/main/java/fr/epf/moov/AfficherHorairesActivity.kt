package fr.epf.moov

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import fr.epf.moov.adapter.ScheduleAdapter
import fr.epf.moov.adapter.StationChoiceAdapter
import fr.epf.moov.data.AppDatabase
import fr.epf.moov.data.StationDao
import fr.epf.moov.model.Station
import fr.epf.moov.service.RATPService
import fr.epf.moov.service.retrofit
import kotlinx.android.synthetic.main.activity_horaires.aller_textview
import kotlinx.android.synthetic.main.activity_horaires.global_schedule_layout
import kotlinx.android.synthetic.main.activity_horaires.pictogram_imageview
import kotlinx.android.synthetic.main.activity_horaires.retour_textview
import kotlinx.android.synthetic.main.activity_horaires.schedules_recyclerview
import kotlinx.android.synthetic.main.activity_horaires.station_name_textview
import kotlinx.android.synthetic.main.activity_schedules_qrcode.*
import kotlinx.coroutines.runBlocking

class AfficherHorairesActivity : AppCompatActivity(){

    private lateinit var nameStation: String
    var allStations: List<Station>? = null
    private var stationDao: StationDao? = null
    val service = retrofit().create(RATPService::class.java)
    var listDestinations: List<String>? = null
    var stringDestinations: String = ""
    private lateinit var station: Station
    var schedulesList: MutableList<String> = mutableListOf()
    var listStations : MutableList<Station> = mutableListOf()
    private var savedStationDao: StationDao? = null
    var way: String = "A"



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedules_qrcode)

        val database =
            Room.databaseBuilder(this, AppDatabase::class.java, "listStations")
                .build()
        stationDao = database.getStationDao()

        val databasesaved =
            Room.databaseBuilder(this, AppDatabase::class.java, "savedStations")
                .build()
        savedStationDao = databasesaved.getStationDao()

        nameStation = intent.getStringExtra("station")
        Log.d("station",nameStation)

        schedules_recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        station_choice_recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)







        runBlocking {
            allStations = stationDao?.getStations()
        }
        var slug = ""
        var type = ""
        var code = ""
        var favoris = false
        allStations?.forEach {
            Log.d("station", it.nameStation)
            if (it.nameStation == nameStation) {
                Log.d("station","true")
                slug = it.slugStation
                type = it.typeLine
                code = it.codeLine
                favoris = false
                runBlocking {
                    val result = service.getStation(type, code)
                    stringDestinations = result.result.directions
                }

                station = Station(
                    0,
                    type,
                    code,
                    nameStation,
                    slug,
                    stringDestinations,
                    favoris
                )
                listStations.add(station)

            }
        }

        station_choice_recyclerview.adapter =
            StationChoiceAdapter(listStations) { station: Station ->
                stationClicked(station)
            }

     /*   if (listStations.size == 1){
            choice_station_layout.visibility = View.GONE
        }*/




    }

    fun getListDestinations(destinations: String?): List<String>? {
        if (destinations != null) {
            return destinations.split(" / ")
        }
        return null
    }



    private fun stationClicked(station : Station){
        val stationclicked : Station = station
        runBlocking {
            val result = service.getSchedules(stationclicked.typeLine, stationclicked.codeLine, stationclicked.slugStation, way)
            schedulesList.clear()


            result.result.schedules.map {
                var schedule = it.message
                schedulesList.add(schedule)

            }
        }

        schedules_recyclerview.adapter =
            ScheduleAdapter(schedulesList)

        listDestinations = getListDestinations(stationclicked.directionLine)

        station_name_textview.text = stationclicked.nameStation
        aller_textview.text = listDestinations?.get(0)
        retour_textview.text = listDestinations?.get(1)

        var resources: Resources = this.resources

        if(stationclicked.typeLine == "metros") {
            val drawableName: String = "m${stationclicked.codeLine}"
            val id: Int =
                resources.getIdentifier(drawableName, "drawable", this.packageName)
            pictogram_imageview.setImageResource(id)
        }
        if(stationclicked.typeLine == "rers") {
            val newCode = stationclicked.codeLine.toLowerCase()
            val drawableName : String = "m${newCode}"
            val id: Int =
                resources.getIdentifier(drawableName, "drawable", this.packageName)
            pictogram_imageview.setImageResource(id)
        }
        if(stationclicked.typeLine == "tramways") {
            val drawableName : String = "t${stationclicked.codeLine}"
            val id: Int =
                resources.getIdentifier(drawableName, "drawable", this.packageName)
            pictogram_imageview.setImageResource(id)
        }

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
                    val result = service.getSchedules(stationclicked.typeLine, stationclicked.codeLine, stationclicked.slugStation, way)
                    schedulesList.clear()
                    result.result.schedules.map {
                        var schedule = it.message
                        schedulesList.add(schedule)
                    }
                }
                schedules_recyclerview.adapter =
                    ScheduleAdapter(schedulesList)
                aller_textview.text = listDestinations?.get(1)
                retour_textview.text = listDestinations?.get(0)
            } else if (way == "R") {
                way = "A"
                runBlocking {
                    val result = service.getSchedules(stationclicked.typeLine, stationclicked.codeLine, stationclicked.slugStation, way)
                    schedulesList.clear()
                    result.result.schedules.map {
                        var schedule = it.message
                        schedulesList.add(schedule)
                    }
                }
                schedules_recyclerview.adapter =
                    ScheduleAdapter(schedulesList)
                aller_textview.text = listDestinations?.get(0)
                retour_textview.text = listDestinations?.get(1)
            }
        }
    }

    }


