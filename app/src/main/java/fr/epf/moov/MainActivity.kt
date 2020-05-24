package fr.epf.moov

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        horaires_button.setOnClickListener {
            val intent = Intent(this, RechercheHorairesActivity::class.java)
            startActivity(intent)
        }

        lists_button.setOnClickListener {
            val intent = Intent(this, ListesActivity::class.java)
            startActivity(intent)
        }

        QRCode_button.setOnClickListener {
            val intent = Intent(this, QRCodeActivity::class.java)
            startActivity(intent)
        }

        favoris_button.setOnClickListener {
            val intent = Intent(this, FavorisActivity::class.java)
            startActivity(intent)
        }
    }
}
