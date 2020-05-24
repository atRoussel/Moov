package fr.epf.moov

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_detail_station.*

class DetailStationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_station)

        val code = intent.getStringExtra("code")
        val name = intent.getStringExtra("name")
        traffic_message_view.text = name
    }
}