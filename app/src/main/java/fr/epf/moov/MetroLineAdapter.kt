package fr.epf.moov

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.epf.moov.model.MetroLine
import kotlinx.android.synthetic.main.activity_horaires.*
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

        val drawableName : String = "m${metro.code}"
        var resources: Resources = context.resources
        val id: Int =
            resources.getIdentifier(drawableName, "drawable", context.packageName)
        holder.metroLineView.metroLine_imageview.setImageResource(id)
        val directions_line = metro.directions.split(" / ")

        holder.metroLineView.metroLine_directionsA_text.text=directions_line[1]
        holder.metroLineView.metroLine_directionsR_text.text=directions_line[0]

        holder.metroLineView.setOnClickListener{
            val intent = Intent(it.context, DetailLineActivity::class.java)
            intent.putExtra("directions", metro.directions)
            intent.putExtra("code", metro.code)
            it.context.startActivity(intent)
        }
    }
}