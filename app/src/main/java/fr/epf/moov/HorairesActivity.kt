package fr.epf.moov

import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatAutoCompleteTextView
import kotlinx.android.synthetic.main.activity_horaires.*
import androidx.recyclerview.widget.LinearLayoutManager
import fr.epf.moov.data.StationDao
import fr.epf.moov.model.Station
import fr.epf.moov.service.RATPService
import fr.epf.moov.service.ScheduleAdapter
import kotlinx.coroutines.runBlocking


class HorairesActivity : AppCompatActivity() {

    var schedulesList: MutableList<MutableList<String>> = mutableListOf()
    var parametersList: MutableList<String> = mutableListOf()
    var allStations: MutableList<Station> = mutableListOf()
    var allStationsName : MutableList<String> = mutableListOf()
    private var stationDao: StationDao? = null
    val service = retrofit().create(RATPService::class.java)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_horaires)

        schedules_recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        //Récupération de toutes les stations

        runBlocking {
            val result = service.listLinesMetros("metros")

            result.result.metros?.map {
                val code = it.code
                val stationsList = service.listStationsMetros("metros", code)
                stationsList.result.stations.map {
                    val station = Station(
                        0,
                        "metros",
                        code,
                        it.name,
                        it.slug
                    )
                    allStations.add(station) //TODO : a mettre dans la base de données
                    allStationsName.add(it.name)
                }
            }
        }


        val adapter : ArrayAdapter<String> = ArrayAdapter(this,android.R.layout.simple_list_item_1,allStationsName)
        autoComplete_stations.setAdapter(adapter)


        schedules_button.setOnClickListener {
            val stationName = autoComplete_stations.text.toString()
            var slug = ""
            var type = ""
            var code = ""

            allStations.forEach {
                if(it.nameStation == stationName) {
                    slug = it.slugStation
                    type = it.typeLine
                    code = it.codeLine
                    Log.d("Stations", slug)
                }
            }
            parametersList.clear()

            parametersList.add(code)
            parametersList.add(stationName)

            runBlocking {
                val result = service.getSchedules(type, code, slug, "A")
                Log.d("Stations", result.toString())
                schedulesList.clear()

                result.result.schedules.map {
                    var schedule: MutableList<String> = mutableListOf()
                    schedule.add(it.message)
                    schedule.add(it.destination)

                    schedulesList.add(schedule)

                }
            }

            schedules_recyclerview.adapter = ScheduleAdapter(parametersList, schedulesList)

        }


    }

    override fun onResume() {
        super.onResume()
    }
}