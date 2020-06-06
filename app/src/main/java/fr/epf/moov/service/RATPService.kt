package fr.epf.moov.service

import retrofit2.http.GET
import retrofit2.http.Path


interface RATPService {
    @GET("v4/lines/{type}")
    suspend fun listLinesMetros(@Path("type") type: String): ModelRatp.Welcome

    @GET("v4/schedules/{type}/{code}/{station}/{way}")
    suspend fun getSchedules(
        @Path("type") type: String,
        @Path("code") code: String,
        @Path("station") station: String,
        @Path("way") way: String
    ): ScheduleModel.Welcome

    @GET("v4/stations/{type}/{code}")
    suspend fun listStationsMetros(
        @Path("type") type: String,
        @Path("code") code: String
    ): StationModel.Welcome

    @GET("v4/lines/{type}/{code}")
    suspend fun getStation(
        @Path("type") type: String,
        @Path("code") code: String
    ): LineModel.Welcome

    @GET("v4/traffic/{type}")
    suspend fun getTraffic(
        @Path("type") type: String
    ): TrafficModel.Welcome

    @GET("v4/traffic/{type}/{code}")
    suspend fun getTrafficLine(
        @Path("type") type: String,
        @Path("code") code: String
    ): TrafficLineModel.Welcome
}


object ModelRatp {
    data class Welcome(
        val result: type,
        val _metadata: Metadata
    )

    data class type(
        val tramways: List<Metro>?,
        val rers: List<Metro>?,
        val metros: List<Metro>?
    )

    data class Metro(
        val code: String,
        val name: String,
        val directions: String,
        val id: Int
    )

    data class Metadata(
        val call: String,
        val date: String,
        val version: Int
    )
}

object LineModel {
    data class Welcome(
        val result: Result,
        val _metadata: Metadata
    )

    data class Result(
        val code: String,
        val name: String,
        val directions: String,
        val id: Int
    )

    data class Metadata(
        val call: String,
        val date: String,
        val version: Int
    )
}