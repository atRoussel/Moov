package fr.epf.moov.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.epf.moov.ImageActivity
import fr.epf.moov.R
import kotlinx.android.synthetic.main.map_view.view.*

class MapAdapter (val maps : MutableList<String>) : RecyclerView.Adapter<MapAdapter.MapViewHolder>() {

    private lateinit var context: Context

    class MapViewHolder(val mapView : View) : RecyclerView.ViewHolder(mapView)

    override fun getItemCount(): Int = maps.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view : View = layoutInflater.inflate(R.layout.map_view,parent, false)

        context  =parent.context

        return MapViewHolder(view)
    }

    override fun onBindViewHolder(holder: MapViewHolder, position: Int) {
        val map = maps[position]

        var name = ""
        var wich = map.substringAfter("map_")
        if(wich[0] =='m'){
            val code = wich.substringAfter('m')
            name = "Métro $code"
        }else if(wich[0] == 'r'){
            val code = wich.substringAfter('r')
            name = "RER ${code.toUpperCase()}"
        }else if(wich[0] == 't'){
            val code = wich.substringAfter('t')
            name = "Tram $code"
        }


        holder.mapView.map_textview.text = name
        val id: Int =
            context.resources.getIdentifier(map, "drawable", context.packageName)
        holder.mapView.map_imageview.setImageResource(id)


        holder.mapView.map_plus_button.setOnClickListener {
            val intent = Intent(context, ImageActivity::class.java)
            intent.putExtra("map", map)
            context.startActivity(intent)
        }




    }

}