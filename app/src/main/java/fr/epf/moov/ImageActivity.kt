package fr.epf.moov

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.github.chrisbanes.photoview.PhotoView


class ImageActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image)


        val photoView = findViewById<View>(R.id.image_view) as PhotoView
        photoView.setImageResource(R.drawable.map)
    }
}