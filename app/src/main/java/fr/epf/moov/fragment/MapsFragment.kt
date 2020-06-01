package fr.epf.moov.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import fr.epf.moov.ImageActivity
import fr.epf.moov.R


class MapsFragment : Fragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_maps, container, false)
val mapPlusButton = view.findViewById<FloatingActionButton>(R.id.map_plus_button)

        mapPlusButton.setOnClickListener{
            val intent = Intent(requireContext(), ImageActivity::class.java)
            startActivity(intent)
        }

        return view
    }
}
