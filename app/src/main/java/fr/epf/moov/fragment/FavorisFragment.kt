package fr.epf.moov.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import fr.epf.moov.R
import fr.epf.moov.adapter.StationFavAdapter
import fr.epf.moov.data.AppDatabase
import fr.epf.moov.data.StationDao
import fr.epf.moov.model.Station
import kotlinx.coroutines.runBlocking

class FavorisFragment : Fragment() {

    private var savedStationDao: StationDao? = null
    var favStations: List<Station>? = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favoris, container, false)
        val emptyFavorisLayout = view.findViewById<LinearLayout>(R.id.layout_empty_favoris)
        var stationRecyclerview = view.findViewById<RecyclerView>(R.id.station_recyclerview)

        emptyFavorisLayout.visibility = View.GONE

        stationRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val databasesaved =
            Room.databaseBuilder(requireContext(), AppDatabase::class.java, "savedStations")
                .build()

        savedStationDao = databasesaved.getStationDao()

        runBlocking {
            favStations = savedStationDao?.getStations()
        }

        stationRecyclerview.adapter =
            StationFavAdapter(favStations)

        if(favStations.isNullOrEmpty()){
            emptyFavorisLayout.visibility = View.VISIBLE
        } else{
            stationRecyclerview.visibility = View.VISIBLE
        }


            return view
    }
}