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

    private var mapsListRER : MutableList<String> = mutableListOf()
    private var mapsListMetro : MutableList<String> = mutableListOf()
    private var mapsListTram : MutableList<String> = mutableListOf()

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

        mapsListMetro.clear()
        mapsListMetro.add("map_m1")
        mapsListMetro.add("map_m2")
        mapsListMetro.add("map_m3")
        mapsListMetro.add("map_m3b")
        mapsListMetro.add("map_m4")
        mapsListMetro.add("map_m5")
        mapsListMetro.add("map_m6")
        mapsListMetro.add("map_m7")
        mapsListMetro.add("map_m7b")
        mapsListMetro.add("map_m8")
        mapsListMetro.add("map_m9")
        mapsListMetro.add("map_m10")
        mapsListMetro.add("map_m11")
        mapsListMetro.add("map_m12")
        mapsListMetro.add("map_m13")
        mapsListMetro.add("map_m14")


        mapMetrosRecyclerview.adapter =
            MapAdapter(mapsListMetro)

        mapsListRER.clear()
        mapsListRER.add("map_ra")
        mapsListRER.add("map_rb")
        mapsListRER.add("map_rc")
        mapsListRER.add("map_rd")
        mapsListRER.add("map_re")

        mapRersRecyclerview.adapter =
            MapAdapter(mapsListRER)

        mapsListTram.clear()
        mapsListTram.add("map_t1")
        mapsListTram.add("map_t2")
        mapsListTram.add("map_t3a")
        mapsListTram.add("map_t3b")
        mapsListTram.add("map_t4")
        mapsListTram.add("map_t5")
        mapsListTram.add("map_t6")
        mapsListTram.add("map_t7")
        mapsListTram.add("map_t8")

        mapTramsRecyclerview.adapter =
            MapAdapter(mapsListTram)

        return view
    }
}
