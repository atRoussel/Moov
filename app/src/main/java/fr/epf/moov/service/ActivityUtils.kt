package fr.epf.moov.service

import android.content.Context
import android.util.Log
import android.widget.Adapter
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.pixplicity.sharp.Sharp
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.io.InputStream


fun AppCompatActivity.fetchSvg(context: Context, url : String, imageView: ImageView) {
    val client = OkHttpClient()

    var request: Request = Request.Builder().url(url).build()

    client.newCall(request).enqueue(object : Callback {
        override fun onFailure(call: Call, e: IOException) {
            Log.d("ERREUR", "erreur image")
        }

        override fun onResponse(call: Call, response: Response) {
            val stream: InputStream? = response.body?.byteStream()
            Sharp.loadInputStream(stream).into(imageView);
            stream?.close()
        }
    })
}

    fun AppCompatActivity.retrofit(): Retrofit {
        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        return Retrofit.Builder()
            .baseUrl("https://api-ratp.pierre-grimaud.fr/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }


fun Adapter.retrofit(): Retrofit {
    val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val client = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
        .addNetworkInterceptor(StethoInterceptor())
        .build()

    return Retrofit.Builder()
        .baseUrl("https://api-ratp.pierre-grimaud.fr/")
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
}



