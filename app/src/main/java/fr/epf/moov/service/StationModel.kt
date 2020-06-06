package fr.epf.moov.service

object StationModel {
    data class Welcome(
        val result: Stations,
        val _metadata: ModelRatp.Metadata
    )

    data class Stations(val stations: List<Station>)

    data class Station(
        val name: String,
        val slug: String
    )
}