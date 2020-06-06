package fr.epf.moov.service

object ScheduleModel {
    data class Welcome(
        val result: schedules,
        val _metadata: Metadata
    )

    data class schedules(val schedules: List<Schedule>)

    data class Schedule(
        val message: String,
        val destination: String
    )

    data class Metadata(
        val call: String,
        val date: String,
        val version: Int
    )
}