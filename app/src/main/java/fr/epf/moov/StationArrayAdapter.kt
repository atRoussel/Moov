package fr.epf.moov

import android.content.Context
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import fr.epf.moov.model.Station

class StationArrayAdapter (context : Context, ressourceId : Int, var stationList : List<Station>) : ArrayAdapter<Station>(context, ressourceId, stationList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {

        var station = stationList[position]
        var view = convertView
        if (convertView == null) {
            var layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
            view = layoutInflater.inflate(R.layout.station_autocomplete_row, parent, false);
        }

        val txt: TextView = view!!.findViewById(R.id.text_autocomplete)
        val img: ImageView = view!!.findViewById(R.id.pictogram_autocomplete)

        if (txt != null) {
            txt.text = station.nameStation
        }

        var resources: Resources = context.resources
        val id: Int =
            resources.getIdentifier("m${station.codeLine}", "drawable", context.packageName)
        img.setImageResource(id)


        return view
    }


}




