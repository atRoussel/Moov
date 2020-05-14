package fr.epf.moov.data

import androidx.room.Database
import androidx.room.RoomDatabase
import fr.epf.moov.model.Station

@Database(entities = [Station::class], version= 1)

abstract class AppDataBase : RoomDatabase() {

    abstract fun getStationDao() : StationDao
}