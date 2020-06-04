package fr.epf.moov

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.android.synthetic.main.activity_image.*


class ImageActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        val map = intent.getStringExtra("map")
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val id = resources.getIdentifier(map, "drawable", packageName)
        image_view.setImageResource(id)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}