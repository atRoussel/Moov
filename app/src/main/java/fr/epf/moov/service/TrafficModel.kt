package fr.epf.moov.service

object TrafficModel {
    data class Welcome (
        val result : type,
        val _metadata : Metadata
    )

    data class type(
        val metros : List<Traffic_line>?,
        val rers : List<Traffic_line>?,
        val tramways : List<Traffic_line>?)

    data class Traffic_line (
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