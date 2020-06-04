package fr.epf.moov.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import fr.epf.moov.ListesActivity
import fr.epf.moov.R
import fr.epf.moov.adapter.MetroLineAdapter
import fr.epf.moov.model.MetroLine
import fr.epf.moov.model.Traffic
import fr.epf.moov.service.RATPService
import fr.epf.moov.service.retrofit
import kotlinx.android.synthetic.main.activity_listes.*
import kotlinx.coroutines.runBlocking


class ListFragment : Fragment() {

    val service = retrofit().create(RATPService::class.java)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.activity_listes, container,false)
        val tramButton = view.findViewById<ImageView>(R.id.bouton_tramway)
        val metroButton = view.findViewById<ImageView>(R.id.bouton_metro)
        val rerButton = view.findViewById<ImageView>(R.id.bouton_rer)
        var type = "metros"

        metroButton.setOnClickListener{
            type = "metros"
            cardview_metro.elevation = 0F
            cardview_rer.elevation = 10F
            cardview_tramway.elevation = 10F

            runBlocking {
                val result = service.listLinesMetros(type)
                MetroLine.all.clear()
                Traffic.all.clear()
                result.result.metros?.map {
                    if ("${it.code}" != "Fun" && "${it.code}" != "Orv") {
                        MetroLine.all.add(
                            MetroLine("${it.code}", "${it.name}", "${it.directions}", it.id, type)
                        )
                    }
                }
                val result_traffic = service.getTraffic("metros")
                result_traffic.result.metros?.map {
                    Traffic.all.add(
                        Traffic(
                            "${it.line}",
                            "${it.slug}",
                            "${it.title}",
                            "${it.message}"
                        )
                    )
                }
            }

            metroLines_recyclerview.adapter =
                MetroLineAdapter(MetroLine.all)

            metroLines_recyclerview.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        rerButton.setOnClickListener{
            type = "rers"
            cardview_metro.elevation = 10F
            cardview_rer.elevation = 0F
            cardview_tramway.elevation = 10F
            runBlocking {
                val result = service.listLinesMetros(type)
                MetroLine.all.clear()
                Traffic.all.clear()
                result.result.rers?.map {
                    if ("${it.code}" != "C" && "${it.code}" != "D") {
                        MetroLine.all.add(
                            MetroLine(
                                "${it.code}", "${it.name}", "${it.directions}", it.id, type
                            )
                        )
                    }
                }
                val result_traffic = service.getTraffic("rers")
                result_traffic.result.rers?.map {
                    Traffic.all.add(
                        Traffic(
                            "${it.line}",
                            "${it.slug}",
                            "${it.title}",
                            "${it.message}"
                        )
                    )
                }
            }

            metroLines_recyclerview.adapter =
                MetroLineAdapter(MetroLine.all)

            metroLines_recyclerview.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }

        tramButton.setOnClickListener {
            type = "tramways"
            cardview_metro.elevation = 10F
            cardview_rer.elevation = 10F
            cardview_tramway.elevation = 0F
            runBlocking {
                val result = service.listLinesMetros(type)
                MetroLine.all.clear()
                Traffic.all.clear()
                result.result.tramways?.map {
                    if ("${it.code}" != "11") {
                        MetroLine.all.add(
                            MetroLine(
                                "${it.code}",
                                "${it.name}",
                                "${it.directions}",
                                it.id,
                                type
                            )
                        )
                    }
                }
                val result_traffic = service.getTraffic("tramways")
                result_traffic.result.tramways?.map {
                    Traffic.all.add(
                        Traffic(
                            it.line.toLowerCase(),
                            "${it.slug}",
                            "${it.title}",
                            "${it.message}"
                        )
                    )
                }

                metroLines_recyclerview.adapter =
                    MetroLineAdapter(MetroLine.all)

                metroLines_recyclerview.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            }
        }



        return view
    }
}