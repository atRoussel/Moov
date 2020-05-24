package fr.epf.moov.adapter

import android.content.Context
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.epf.moov.R
import fr.epf.moov.model.Station
import kotlinx.android.synthetic.main.station_choice_view.view.*

class StationChoiceAdapter(val listStations: List<Station>, val clickListener : (Station) -> Unit) : RecyclerView.Adapter<StationChoiceAdapter.PopUpViewHolder>() {

    private lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopUpViewHolder {
        val layoutInfater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInfater.inflate(R.layout.station_choice_view, parent, false)
        context = parent.context
        return PopUpViewHolder(view)
    }

        override fun getItemCount(): Int = listStations.size

    override fun onBindViewHolder(holder: PopUpViewHolder, position: Int) {

        val station = listStations[position]
        Log.d("CCCCC", station.toString())

        val drawableName: String = "m${station.codeLine}"
        var resources: Resources = context.resources

        val id: Int = resources.getIdentifier(drawableName, "drawable", context.packageName)
        holder.popupView.station_choice_image.setImageResource(id)
        (holder as PopUpViewHolder).bind(station!!, clickListener)


    }

    class PopUpViewHolder(val popupView: View) : RecyclerView.ViewHolder(popupView) {

        fun bind(station: Station, clickListener: (Station) -> Unit) {
            popupView.setOnClickListener { clickListener(station) }
        }


    }
}
