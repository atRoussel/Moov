package fr.epf.moov.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import fr.epf.moov.ListesActivity
import fr.epf.moov.R
import fr.epf.moov.service.RATPService
import fr.epf.moov.service.retrofit


class ListFragment : Fragment() {

    val service = retrofit().create(RATPService::class.java)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_lists, container,false)
        val tramButton = view.findViewById<ImageButton>(R.id.tram_button)
        val metroButton = view.findViewById<ImageButton>(R.id.metros_button)
        val rerButton = view.findViewById<ImageButton>(R.id.rers_button)



        metroButton.setOnClickListener{
            val intent = Intent(requireContext(), ListesActivity::class.java)
            intent.putExtra("type", "metros")
            startActivity(intent)
        }

        rerButton.setOnClickListener{
            val intent = Intent(requireContext(), ListesActivity::class.java)
            intent.putExtra("type", "rers")
            startActivity(intent)
        }

        tramButton.setOnClickListener {
            val intent = Intent(requireContext(), ListesActivity::class.java)
            intent.putExtra("type", "tramways")
            startActivity(intent)
        }



        return view

    }
}