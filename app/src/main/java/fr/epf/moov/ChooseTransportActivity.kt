package fr.epf.moov

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_choose_transport.*

class ChooseTransportActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choose_transport)
        tram_button.setImageResource(R.drawable.tramway)
        metros_button.setImageResource(R.drawable.metro)
        rers_button.setImageResource(R.drawable.rer)

        metros_button.setOnClickListener{
            val intent = Intent(this, ListesActivity::class.java)
            intent.putExtra("type", "metros")
            startActivity(intent)
        }

        rers_button.setOnClickListener{
            val intent = Intent(this, ListesActivity::class.java)
            intent.putExtra("type", "rers")
            startActivity(intent)
        }

        tram_button.setOnClickListener {
            val intent = Intent(this, ListesActivity::class.java)
            intent.putExtra("type", "tramways")
            startActivity(intent)
        }

    }
}