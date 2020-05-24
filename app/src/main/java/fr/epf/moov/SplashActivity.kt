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
                        it.slug,
                        null,
                    false)
                    stationDao?.addStation(station)
                }
            }

        }

        runBlocking {
            val result = service.listLinesMetros("metros")
            MetroLine.all.clear()
            result.result.metros?.map {
                if("${it.code}" != "Fun" && "${it.code}" != "Orv") {
                    MetroLine.all.add(MetroLine( "${it.code}","${it.name}", "${it.directions}", it.id))
                }
            }
        }

        runBlocking {
            val result = service.getTraffic("metros")
            Traffic.all.clear()
            result.result.metros.map{
                Traffic.all.add(Traffic("${it.line}", "${it.slug}", "${it.title}", "${it.message}"))
            }
        }

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },2000)
    }

}