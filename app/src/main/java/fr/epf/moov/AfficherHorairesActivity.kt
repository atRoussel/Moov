package fr.epf.moov

import android.content.res.Resources
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import fr.epf.moov.data.AppDatabase
import fr.epf.moov.data.StationDao
import fr.epf.moov.model.Station
import fr.epf.moov.service.RATPService
import fr.epf.moov.service.retrofit
import kotlinx.android.synthetic.main.activity_horaires.*
import kotlinx.android.synthetic.main.choice_popup.*
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
        setContentView(R.layout.activity_horaires)

        val database =
            Room.databaseBuilder(this, AppDatabase::class.java, "listStations")
                .build()
        stationDao = database.getStationDao()

        nameStation = intent.getStringExtra("station")
        Log.d("station",nameStation)

        schedules_recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

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


     /*   if (listStations.size>1){
            showPopUp()
        } */


        runBlocking {
            val result = service.getSchedules(type, code, slug, "A")
            schedulesList.clear()


            result.result.schedules.map {
                var schedule = it.message
                schedulesList.add(schedule)

            }
        }

        schedules_recyclerview.adapter = ScheduleAdapter(schedulesList)

        station_name_textview.text = nameStation
        aller_textview.text = listDestinations?.get(0)
        retour_textview.text = listDestinations?.get(1)


        val drawableName: String = "m${code}"

        var resources: Resources = this.resources
        val id: Int =
            resources.getIdentifier(drawableName, "drawable", this.packageName)
        pictogram_imageview.setImageResource(id)

        global_schedule_layout.visibility = View.VISIBLE
    }

    fun getListDestinations(destinations: String?): List<String>? {
        if (destinations != null) {
            return destinations.split(" / ")
        }
        return null
    }

    fun showPopUp(){
        //Ouverture de la pop-up


        val popup = AlertDialog.Builder(this)
        choice_popup_recyclerview.layoutManager =
            LinearLayoutManager(popup.context, LinearLayoutManager.VERTICAL, false)
        popup.setTitle("Choisissez votre station")
        val view = layoutInflater.inflate(R.layout.choice_popup, null)
        val okbutton = view.findViewById<Button>(R.id.ok_button_popup)

        popup.setView(view)
       // choice_popup_recyclerview.adapter = PopUpAdapter(listStations, this)

        val alert = popup.show()

        okbutton.setOnClickListener {

        }


    }


}