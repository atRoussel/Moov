package fr.epf.moov

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import fr.epf.moov.data.AppDatabase
import fr.epf.moov.data.StationDao
import fr.epf.moov.model.Station
import fr.epf.moov.service.RATPService
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
                        it.slug
                    )
                    stationDao?.addStation(station)
                }
            }

        }


        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        },2000)
    }

}