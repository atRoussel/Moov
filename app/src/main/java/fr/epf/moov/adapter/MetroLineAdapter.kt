package fr.epf.moov.adapter

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import fr.epf.moov.DetailLineActivity
import fr.epf.moov.DetailStationActivity
import fr.epf.moov.R
import fr.epf.moov.model.MetroLine
import fr.epf.moov.model.Traffic
import kotlinx.android.synthetic.main.metroline_view.view.*

class MetroLineAdapter(val metroLines: List<MetroLine>) : RecyclerView.Adapter<MetroLineAdapter.MetroLineViewHolder>() {
    class MetroLineViewHolder(val metroLineView: View) : RecyclerView.ViewHolder(metroLineView)

    private lateinit var context : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetroLineViewHolder {
        val layoutInfater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInfater.inflate(R.layout.metroline_view, parent, false)

        context = parent.context

        return MetroLineViewHolder(view)
    }
    override fun getItemCount(): Int = metroLines.size

    override fun onBindViewHolder(holder: MetroLineViewHolder, position: Int) {
        val metro = metroLines[position]
        var resources: Resources = context.resources

        if(metro.type == "metros") {
            val drawableName : String = "m${metro.code}"
            val id: Int =
                resources.getIdentifier(drawableName, "drawable", context.packageName)
            holder.metroLineView.metroLine_imageview.setImageResource(id)

            if(Traffic.all[position].slug == "normal") {
                holder.metroLineView.warning_imageview.visibility=View.GONE
            }
        }
        if(metro.type == "rers") {
            val newCode = metro.code.toLowerCase()
            val drawableName : String = "m${newCode}"
            val id: Int =
                resources.getIdentifier(drawableName, "drawable", context.packageName)
            holder.metroLineView.metroLine_imageview.setImageResource(id)

            Traffic.all.map{
                if(metro.code == it.line) {
                    if(it.slug == "normal") {
                        holder.metroLineView.warning_imageview.visibility=View.GONE
                    }
                }
            }
        }
        if(metro.type == "tramways") {
            val drawableName : String = "t${metro.code}"
            val id: Int =
                resources.getIdentifier(drawableName, "drawable", context.packageName)
            holder.metroLineView.metroLine_imageview.setImageResource(id)

            Traffic.all.map{
                if(metro.code == it.line || (metro.code == "11")) {
                    if(it.slug == "normal" || (metro.code == "11")) {
                        holder.metroLineView.warning_imageview.visibility=View.GONE
                    }
                }
            }
        }


        val directions_line = metro.directions.split(" / ")

        holder.metroLineView.metroLine_directionsA_text.text=directions_line[1]
        holder.metroLineView.metroLine_directionsR_text.text=directions_line[0]

        holder.metroLineView.setOnClickListener{
            if(metro.code == "C" || metro.code ==  "D") {
                Toast.makeText(this.context, "Indisponible pour le moment", Toast.LENGTH_SHORT).show()
            } else {
                val intent = Intent(it.context, DetailLineActivity::class.java)
                intent.putExtra("type", metro.type)
                intent.putExtra("directions", metro.directions)
                intent.putExtra("code", metro.code)
                it.context.startActivity(intent)
            }

        }
    }
}