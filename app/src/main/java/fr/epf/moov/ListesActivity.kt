package fr.epf.moov

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import fr.epf.moov.service.RATPService
import kotlinx.coroutines.runBlocking
import fr.epf.moov.model.MetroLine
import fr.epf.moov.service.retrofit
import kotlinx.android.synthetic.main.activity_listes.*


class ListesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listes)

        val service = retrofit().create(RATPService::class.java)

        runBlocking {
            val result = service.listLinesMetros("metros")
            Log.d("APIresult", result.toString())

            result.result.metros?.map {
                Log.d("APIresult", "${it.code} / ${it.name}")
                MetroLine.all.add(
                    MetroLine(
                        "${it.code}",
                        "${it.name}",
                        "${it.directions}",
                        it.id
                    )
                )
            }
        }

        //Implementing
        setContentView(R.layout.activity_listes)
        metroLines_recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        metroLines_recyclerview.adapter = MetroLineAdapter(MetroLine.all)


    }}
