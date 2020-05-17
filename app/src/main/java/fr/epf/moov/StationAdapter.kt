package fr.epf.moov

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.station_view.view.*

class StationAdapter(val stationsList: List<Station>) : RecyclerView.Adapter<StationAdapter.StationViewHolder>() {
    class StationViewHolder(val stationView: View) : RecyclerView.ViewHolder(stationView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val layoutInfater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInfater.inflate(R.layout.station_view, parent, false)
        return StationViewHolder(view)
    }
    override fun getItemCount(): Int = stationsList.size

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        val station = stationsList[position]
        holder.stationView.station_name_text.text = "${station.name}"

    }

}