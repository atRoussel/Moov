package fr.epf.moov.data

import androidx.room.Dao
import androidx.room.Query
import fr.epf.moov.model.Station

@Dao
interface StationDao {
    @Query("select * from stations")
    suspend fun getStations() : List<Station>

    @Query ("select * from stations where nameStation = :name")
    suspend fun getStation(name : String) : Station
}