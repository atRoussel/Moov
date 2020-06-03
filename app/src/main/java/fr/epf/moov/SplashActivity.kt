package fr.epf.moov

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import fr.epf.moov.data.AppDatabase
import fr.epf.moov.data.StationDao
import fr.epf.moov.model.MetroLine
import fr.epf.moov.model.Station
import fr.epf.moov.model.Traffic
import fr.epf.moov.service.RATPService
import fr.epf.moov.service.retrofit
import kotlinx.android.synthetic.main.activity_splash.*
import kotlinx.coroutines.runBlocking

class SplashActivity  : AppCompatActivity () {

    private var stationDao : StationDao? = null
    val service = retrofit().create(RATPService::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val database =
            Room.databaseBuilder(this, AppDatabase::class.java, "listStations")
                .build()

        stationDao = database.getStationDao()


        runBlocking {
            stationDao?.deleteStations()
            val result_metros = service.listLinesMetros("metros")

            result_metros.result.metros?.map {
                val code = it.code
                val stationsList = service.listStationsMetros("metros", code)
                stationsList.result.stations.map {
                    val station = Station(
                        0,
                        "metros",
                        code,
                        it.name,
                        it.slug,
                        null,
                    false)
                    stationDao?.addStation(station)
                }
            }

            val result_tramways = service.listLinesMetros("tramways")

            result_tramways.result.tramways?.map {
                val code = it.code
                val stationsList = service.listStationsMetros("tramways", code)
                stationsList.result.stations.map {
                    val station = Station(
                        0,
                        "tramways",
                        code,
                        it.name,
                        it.slug,
                        null,
                        false)
                    stationDao?.addStation(station)
                }

            }

            val result_rerA = service.listStationsMetros("rers", "A")

            result_rerA.result.stations?.map {
                val station = Station(
                    0,
                    "rers",
                    "A",
                    it.name,
                    it.slug,
                    null,
                    false)
                stationDao?.addStation(station)
            }

            val result_rerB = service.listStationsMetros("rers", "B")

            result_rerB.result.stations?.map {
                val station = Station(
                    0,
                    "rers",
                    "B",
                    it.name,
                    it.slug,
                    null,
                    false)
                stationDao?.addStation(station)
            }

            val result_rerE = service.listStationsMetros("rers", "E")

            result_rerE.result.stations?.map {
                val station = Station(
                    0,
                    "rers",
                    "E",
                    it.name,
                    it.slug,
                    null,
                    false)
                stationDao?.addStation(station)
            }

        }


            astuce_layout.text ="Châtelet - Les Halles est la plus grande station de métro au monde. Si les piliers et les affichages vous font zigzaguer et vous perdre c'est voulu : le but est de casser les grands mouvements de foule."



        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },5000)
    }

}