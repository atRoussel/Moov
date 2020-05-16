package fr.epf.moov.service

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RatpApiService {

    @GET("/lines/metros")
    fun listMetros() : Call<List<MetroLine>>
}


data class MetroLine (val code : Int,
                      val name : String,
                      val directions : String,
                      val id : Int)
