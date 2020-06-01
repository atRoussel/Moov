package fr.epf.moov

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.chrisbanes.photoview.PhotoView
import kotlinx.android.synthetic.main.activity_image.*


class ImageActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)
        val map = intent.getStringExtra("map")


        val id = resources.getIdentifier(map, "drawable", packageName)
        image_view.setImageResource(id)
    }
}