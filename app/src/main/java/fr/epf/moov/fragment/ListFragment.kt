package fr.epf.moov.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.epf.moov.ListesActivity
import fr.epf.moov.R
import fr.epf.moov.adapter.MetroLineAdapter
import fr.epf.moov.model.MetroLine
import fr.epf.moov.model.Traffic
import fr.epf.moov.service.RATPService
import fr.epf.moov.service.retrofit
import kotlinx.coroutines.runBlocking

class ListFragment : Fragment() {

    val service = retrofit().create(RATPService::class.java)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_lists, container,false)
        val tramButton = view.findViewById<ImageView>(R.id.tram_button)
        val metroButton = view.findViewById<ImageView>(R.id.metros_button)
        val rerButton = view.findViewById<ImageView>(R.id.rers_button)


        tramButton.setImageResource(R.drawable.tramway)
        metroButton.setImageResource(R.drawable.metro)
        rerButton.setImageResource(R.drawable.rer)

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