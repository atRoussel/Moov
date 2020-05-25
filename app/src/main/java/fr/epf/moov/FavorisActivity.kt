package fr.epf.moov

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import fr.epf.moov.adapter.StationFavAdapter
import fr.epf.moov.data.AppDatabase
import fr.epf.moov.data.StationDao
import fr.epf.moov.model.Station
import kotlinx.android.synthetic.main.activity_favoris.*
import kotlinx.coroutines.runBlocking

class FavorisActivity : AppCompatActivity() {

    private var savedStationDao: StationDao? = null
    var favStations: List<Station>? = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoris)
        layout_empty_favoris.visibility = View.GONE

        station_recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val databasesaved =
            Room.databaseBuilder(this, AppDatabase::class.java, "savedStations")
                .build()

        savedStationDao = databasesaved.getStationDao()

        runBlocking {
            favStations = savedStationDao?.getStations()
        }

        station_recyclerview.adapter =
            StationFavAdapter(favStations)

        if(favStations.isNullOrEmpty()){
            layout_empty_favoris.visibility = View.VISIBLE
        } else{
            station_recyclerview.visibility = View.VISIBLE
        }

    }
}