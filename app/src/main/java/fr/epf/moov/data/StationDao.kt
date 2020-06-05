package fr.epf.moov.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import fr.epf.moov.model.Station

@Dao
interface StationDao {
    @Query("select * from stations")
    suspend fun getStations() : List<Station>

    @Query ("select * from stations where nameStation = :name")
    suspend fun getStation(name : String) : Station

    @Insert
    suspend fun addStation (station: Station)

    @Query("delete from stations")
    suspend fun deleteStations()

    @Query("delete from stations where codeLine = :code and nameStation =:name")
    suspend fun deleteStation(code : String, name:String)


}