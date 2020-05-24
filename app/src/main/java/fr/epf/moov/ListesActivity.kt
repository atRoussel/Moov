package fr.epf.moov

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import fr.epf.moov.adapter.MetroLineAdapter
import fr.epf.moov.model.MetroLine
import kotlinx.android.synthetic.main.activity_listes.*


class ListesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listes)

        metroLines_recyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        metroLines_recyclerview.adapter =
            MetroLineAdapter(MetroLine.all)
    }
}
