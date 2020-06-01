package fr.epf.moov.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.epf.moov.ImageActivity
import fr.epf.moov.R
import fr.epf.moov.adapter.MapAdapter


class MapsFragment : Fragment(){

    private var mapsList : MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_maps, container, false)
        val mapRecyclerview = view.findViewById<RecyclerView>(R.id.maps_recyclerview)

        mapRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)


        mapsList.add("map")
        mapsList.add("map_m1")

        mapRecyclerview.adapter =
            MapAdapter(mapsList)



        return view
    }
}
