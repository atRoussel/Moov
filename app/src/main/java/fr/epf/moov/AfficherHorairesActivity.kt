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



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedules_qrcode)

        val database =
            Room.databaseBuilder(this, AppDatabase::class.java, "listStations")
                .build()
        stationDao = database.getStationDao()

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
                    listDestinations = getListDestinations(stringDestinations)
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



    private fun stationClicked(station : Station){
        Toast.makeText(this, "Clicked: ${station.codeLine}", Toast.LENGTH_SHORT).show()
        val stationclicked : Station = station
        runBlocking {
            val result = service.getSchedules(stationclicked.typeLine, stationclicked.codeLine, stationclicked.slugStation, "A")
            schedulesList.clear()


            result.result.schedules.map {
                var schedule = it.message
                schedulesList.add(schedule)

            }
        }

        schedules_recyclerview.adapter =
            ScheduleAdapter(schedulesList)

        station_name_textview.text = nameStation
        aller_textview.text = listDestinations?.get(0)
        retour_textview.text = listDestinations?.get(1)


        val drawableName: String = "m${stationclicked.codeLine}"

        var resources: Resources = this.resources
        val id: Int =
            resources.getIdentifier(drawableName, "drawable", this.packageName)
        pictogram_imageview.setImageResource(id)

        global_schedule_layout.visibility = View.VISIBLE

    }


}