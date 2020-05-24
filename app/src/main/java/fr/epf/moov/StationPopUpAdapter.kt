package fr.epf.moov

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.epf.moov.model.Station
import kotlinx.android.synthetic.main.station_view.view.*

class StationPopUpAdapter(val stations: List<Station>?) : RecyclerView.Adapter<StationPopUpAdapter.StationPopUpViewHolder>() {

    class StationPopUpViewHolder(val stationPopUpView: View) :
        RecyclerView.ViewHolder(stationPopUpView)

    override fun getItemCount(): Int {
        if (stations != null) return stations.size
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationPopUpViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.station_view, parent, false)
        return StationPopUpViewHolder(view)
    }

    override fun onBindViewHolder(holder: StationPopUpViewHolder, position: Int) {


        val station = stations?.get(position)
        holder.stationPopUpView.station_name_text.text = "${station?.nameStation}"

    }

    public fun getStation(position : Int) : Station {
        return this.stations!![position]
    }
}