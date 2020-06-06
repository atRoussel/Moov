package fr.epf.moov.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.epf.moov.DetailStationActivity
import fr.epf.moov.R
import fr.epf.moov.model.Station
import kotlinx.android.synthetic.main.station_view.view.*

class StationAdapter(val stations: List<Station>?) :
    RecyclerView.Adapter<StationAdapter.StationViewHolder>() {

    class StationViewHolder(val stationView: View) : RecyclerView.ViewHolder(stationView)

    override fun getItemCount(): Int {
        if (stations != null) return stations.size
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.station_view, parent, false)

        return StationViewHolder(view)
    }

    override fun onBindViewHolder(holder: StationViewHolder, position: Int) {
        val station = stations?.get(position)
        holder.stationView.station_name_text.text = "${station?.nameStation}"

        holder.stationView.setOnClickListener {
            val intent = Intent(it.context, DetailStationActivity::class.java)
            intent.putExtra("type", station?.typeLine)
            intent.putExtra("slug", station?.slugStation)
            intent.putExtra("name", station?.nameStation)
            intent.putExtra("code", station?.codeLine)
            it.context.startActivity(intent)
        }
    }
}

