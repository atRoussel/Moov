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
import kotlinx.android.synthetic.main.map_view.view.*


class MapsFragment : Fragment(){

    private var mapsList : MutableList<String> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_maps, container, false)
        val mapMetrosRecyclerview = view.findViewById<RecyclerView>(R.id.maps_metros_recyclerview)
        val mapRersRecyclerview = view.findViewById<RecyclerView>(R.id.maps_rers_recyclerview)
        val mapTramsRecyclerview = view.findViewById<RecyclerView>(R.id.maps_trams_recyclerview)
        val plusButton = view.findViewById<FloatingActionButton>(R.id.map_plus_button)

        mapMetrosRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mapRersRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        mapTramsRecyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)


        plusButton.setOnClickListener {
            val intent = Intent(context, ImageActivity::class.java)
            intent.putExtra("map", "map")
            startActivity(intent)
        }


        //TODO charger dans le splash
        mapsList.add("map_m1")
        mapsList.add("map_m2")
        mapsList.add("map_m3")
        mapsList.add("map_m3b")
        mapsList.add("map_m4")
        mapsList.add("map_m5")
        mapsList.add("map_m6")
        mapsList.add("map_m7")



        mapMetrosRecyclerview.adapter =
            MapAdapter(mapsList)
        mapRersRecyclerview.adapter =
            MapAdapter(mapsList)
        mapTramsRecyclerview.adapter =
            MapAdapter(mapsList)



        return view
    }
}
