package fr.epf.moov

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import fr.epf.moov.adapter.MetroLineAdapter
import fr.epf.moov.model.MetroLine
import fr.epf.moov.model.Traffic
import kotlinx.android.synthetic.main.activity_listes.*
import kotlinx.coroutines.runBlocking
import fr.epf.moov.service.RATPService
import fr.epf.moov.service.retrofit


class ListesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listes)
        val service = retrofit().create(RATPService::class.java)
        val type = intent.getStringExtra("type")

        metroLines_recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        runBlocking {
            val result = service.listLinesMetros(type)
            MetroLine.all.clear()
            Traffic.all.clear()

            if(type == "metros") {
                type_image.setImageResource(R.drawable.metro)
                result.result.metros?.map {
                    if("${it.code}" != "Fun" && "${it.code}" != "Orv") {
                        MetroLine.all.add(MetroLine( "${it.code}","${it.name}", "${it.directions}", it.id, type))
                    }
                }
                val result_traffic = service.getTraffic("metros")
                result_traffic.result.metros?.map{
                    Traffic.all.add(Traffic("${it.line}", "${it.slug}", "${it.title}", "${it.message}"))
                }
            }
            if( type == "rers") {
                type_image.setImageResource(R.drawable.rer)
                result.result.rers?.map {
                    MetroLine.all.add(MetroLine( "${it.code}","${it.name}", "${it.directions}", it.id, type))
                }
                val result_traffic = service.getTraffic("rers")
                result_traffic.result.rers?.map{
                    Traffic.all.add(Traffic("${it.line}", "${it.slug}", "${it.title}", "${it.message}"))
                }
            }
            if( type == "tramways") {
                type_image.setImageResource(R.drawable.tramway)
                result.result.tramways?.map {
                    MetroLine.all.add(MetroLine( "${it.code}","${it.name}", "${it.directions}", it.id, type))
                }
                val result_traffic = service.getTraffic("tramways")
                result_traffic.result.tramways?.map{
                    Traffic.all.add(Traffic("${it.line}", "${it.slug}", "${it.title}", "${it.message}"))
                }
            }

        }

        metroLines_recyclerview.adapter =
            MetroLineAdapter(MetroLine.all)
    }
}
