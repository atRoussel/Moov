package fr.epf.moov.service

import com.squareup.moshi.JsonAdapter
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface RATPService {
    @GET("v4/lines/{type}")
    suspend fun listLinesMetros(@Path("type") type : String) : ModelRatp.Welcome
}





object ModelRatp {
    data class Welcome (
        val result : metros,

        val _metadata : Metadata)

data class metros (
    val metros : List<Metro>?)

    data class Metro(val code : String,
    val name : String,
    val directions : String,
    val id : Int)

    data class Metadata (
        val call : String,
        val date : String,
        val version : Int
    )
}