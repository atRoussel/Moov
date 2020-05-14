package fr.epf.moov

import androidx.appcompat.app.AppCompatActivity
import com.facebook.stetho.okhttp3.StethoInterceptor
import com.squareup.moshi.FromJson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory



fun AppCompatActivity.retrofit() : Retrofit {
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


