package fr.epf.moov

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import fr.epf.moov.data.AppDatabase
import fr.epf.moov.data.StationDao
import fr.epf.moov.model.Station
import fr.epf.moov.service.RATPService
import fr.epf.moov.service.retrofit
import kotlinx.android.synthetic.main.stationfav_view.view.*
import kotlinx.coroutines.runBlocking

class StationFavAdapter(val stations: List<Station>?) : RecyclerView.Adapter<StationFavAdapter.StationFavViewHolder>()  {

    private lateinit var context : Context
    private var savedStationDao: StationDao? = null

    class StationFavViewHolder(val stationFavView: View) : RecyclerView.ViewHolder(stationFavView)

    override fun getItemCount(): Int {
        if (stations != null) return stations.size
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationFavViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.stationfav_view, parent, false)

        context = parent.context

        val databasesaved =
            Room.databaseBuilder(context, AppDatabase::class.java, "savedStations")
                .build()

        savedStationDao = databasesaved.getStationDao()



        return StationFavViewHolder(view)
    }

    override fun onBindViewHolder(holder: StationFavViewHolder, position: Int) {
        val station = stations?.get(position)
        val stringDestinations = getListDestinations(station?.directionLine)

        holder.stationFavView.stationfav_name_textview.text = "${station?.nameStation}"
        holder.stationFavView.aller_textview.text = "${stringDestinations?.get(0)}"
        holder.stationFavView.retour_textview.text = "${stringDestinations?.get(1)}"

        val drawableName: String = "m${station?.codeLine}"

        var resources: Resources = context.resources
        val id: Int =
            resources.getIdentifier(drawableName, "drawable", context.packageName)
        holder.stationFavView.pictogram_imageview.setImageResource(id)

        holder.stationFavView.fav_imageview.setImageResource(R.drawable.fav_full)


        holder.stationFavView.fav_imageview.setOnClickListener {
            if (station?.favoris == true){
                station?.favoris=false
                runBlocking {
                    savedStationDao?.deleteStation(station?.id)
                }

                holder.stationFavView.fav_imageview.setImageResource(R.drawable.fav_empty)
                Toast.makeText(context, "La station a été supprimée des favoris", Toast.LENGTH_SHORT).show()

            } else if (station?.favoris == false){
                station?.favoris = true

                runBlocking {
                    savedStationDao?.addStation(station)
                }

                holder.stationFavView.fav_imageview.setImageResource(R.drawable.fav_full)
                Toast.makeText(context, "La station a été rajoutée aux favoris", Toast.LENGTH_SHORT).show()

            }
        }

    }
}

fun getListDestinations(destinations: String?): List<String>? {
    if (destinations != null) {
        return destinations.split(" / ")
    }
    return null
}
