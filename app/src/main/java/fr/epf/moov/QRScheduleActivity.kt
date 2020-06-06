package fr.epf.moov

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
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
import kotlinx.android.synthetic.main.activity_schedules_qrcode.*
import kotlinx.coroutines.runBlocking

class QRScheduleActivity : AppCompatActivity() {

    private lateinit var nameStation: String
    var allStations: List<Station>? = null
    var savedStations: List<Station>? = null
    private var stationDao: StationDao? = null
    val service = retrofit().create(RATPService::class.java)
    var listDestinations: List<String>? = null
    var stringDestinations: String = ""
    private lateinit var station: Station
    var schedulesList: MutableList<String> = mutableListOf()
    var listStations: MutableList<Station> = mutableListOf()
    private var savedStationDao: StationDao? = null
    var way: String = "A"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedules_qrcode)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setLogo(R.drawable.logo_moov_mini)

        val database =
            Room.databaseBuilder(this, AppDatabase::class.java, "listStations")
                .build()
        stationDao = database.getStationDao()

        val databasesaved =
            Room.databaseBuilder(this, AppDatabase::class.java, "savedStations")
                .build()
        savedStationDao = databasesaved.getStationDao()

        nameStation = intent.getStringExtra("station")
        Log.d("station", nameStation)

        schedules_recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        station_choice_recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        runBlocking {
            allStations = stationDao?.getStations()
            savedStations = savedStationDao?.getStations()
        }
        var slug = ""
        var type = ""
        var code = ""
        var favoris = false

        allStations?.forEach {
            if (it.nameStation == nameStation) {
                slug = it.slugStation
                type = it.typeLine
                code = it.codeLine
                favoris = false
                runBlocking {
                    val result = service.getStation(type, code)
                    stringDestinations = result.result.directions
                }

                savedStations?.forEach {
                    if (it.nameStation == nameStation && it.codeLine == code)
                        favoris = true
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
    }

    fun getListDestinations(destinations: String?): List<String>? {
        if (destinations != null) {
            return destinations.split(" / ")
        }
        return null
    }

    private fun stationClicked(station: Station) {
        val stationclicked: Station = station
        runBlocking {
            if (station.favoris == true) fav_imageview.setImageResource(R.drawable.fav_full)
            else fav_imageview.setImageResource(R.drawable.fav_empty)
            global_schedule_layout.visibility = View.VISIBLE
            val result = service.getSchedules(
                stationclicked.typeLine,
                stationclicked.codeLine,
                stationclicked.slugStation,
                way
            )
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

        if (stationclicked.typeLine == "metros") {
            val drawableName: String = "m${stationclicked.codeLine}"
            val id: Int =
                resources.getIdentifier(drawableName, "drawable", this.packageName)
            pictogram_imageview.setImageResource(id)
        }
        if (stationclicked.typeLine == "rers") {
            val newCode = stationclicked.codeLine.toLowerCase()
            val drawableName: String = "m${newCode}"
            val id: Int =
                resources.getIdentifier(drawableName, "drawable", this.packageName)
            pictogram_imageview.setImageResource(id)
        }
        if (stationclicked.typeLine == "tramways") {
            val drawableName: String = "t${stationclicked.codeLine}"
            val id: Int =
                resources.getIdentifier(drawableName, "drawable", this.packageName)
            pictogram_imageview.setImageResource(id)
        }

        if (station.favoris == true)
            fav_imageview.setImageResource(R.drawable.fav_full)
        global_schedule_layout.visibility = View.VISIBLE

        fav_imageview.setOnClickListener {
            if (station.favoris == true) {
                station.favoris = false
                fav_imageview.setImageResource(R.drawable.fav_empty)
                Log.d("FAVSTATION", station.toString())
                runBlocking {
                    savedStationDao?.deleteStation(station.codeLine, station.nameStation)
                }
                Toast.makeText(this, "La station a été supprimée des favoris", Toast.LENGTH_SHORT)
                    .show()
            } else if (station.favoris == false) {
                station.favoris = true
                fav_imageview.setImageResource(R.drawable.fav_full)
                runBlocking {
                    savedStationDao?.addStation(station)
                }
                Toast.makeText(this, "La station a été ajoutée aux favoris", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        destinations_exchange.setOnClickListener {
            if (way == "A") {
                way = "R"
                runBlocking {
                    val result = service.getSchedules(
                        stationclicked.typeLine,
                        stationclicked.codeLine,
                        stationclicked.slugStation,
                        way
                    )
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
                    val result = service.getSchedules(
                        stationclicked.typeLine,
                        stationclicked.codeLine,
                        stationclicked.slugStation,
                        way
                    )
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


