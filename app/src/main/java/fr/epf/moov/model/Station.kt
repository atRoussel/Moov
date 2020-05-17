package fr.epf.moov.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stations")
data class Station ( @PrimaryKey (autoGenerate = true) val id : Int,
                     val typeLine : String,
                     val codeLine : String,
                     val nameStation : String,
                     val slugStation : String,
                     val directionLine : String?,
                     var favoris : Boolean)

