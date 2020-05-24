package fr.epf.moov

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.epf.moov.model.Station
import kotlinx.android.synthetic.main.choice_station_view.view.*

class PopUpAdapter(val listStations: List<Station>, var clickListener: OnStationItemClickListener) : RecyclerView.Adapter<PopUpAdapter.PopUpViewHolder>() {


    private lateinit var context : Context


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopUpAdapter.PopUpViewHolder {
        val layoutInfater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInfater.inflate(R.layout.choice_station_view, parent, false)
        context = parent.context
        return PopUpAdapter.PopUpViewHolder(view)
    }

        override fun getItemCount(): Int = listStations.size

    override fun onBindViewHolder(holder: PopUpViewHolder, position: Int) {
        val station = listStations[position]
        holder.initialize(station, clickListener)

        val drawableName: String = "m${station.codeLine}"

        var resources: Resources = context.resources
        val id: Int =
            resources.getIdentifier(drawableName, "drawable", context.packageName)
        holder.popupView.choice_popup_image.setImageResource(id)
        holder.popupView.station_popup_textview.text = station.nameStation



    }

    class PopUpViewHolder(val popupView: View) : RecyclerView.ViewHolder(popupView){
        fun initialize(item : Station, action : OnStationItemClickListener){
            popupView.setOnClickListener {
                action.onItemClik(item, adapterPosition)
            }
        }
    }


    interface OnStationItemClickListener{
        fun onItemClik(station : Station, position : Int)
    }



}
