package fr.epf.moov.service

object TrafficLineModel {
    data class Welcome (
        val result : Result,
        val _metadata : Metadata
    )

    data class Result (
        val line : String,
        val slug : String,
        val title: String,
        val message: String
    )

    data class Metadata (
        val call : String,
        val date : String,
        val version : Int
    )
}