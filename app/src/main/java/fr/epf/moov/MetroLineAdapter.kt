package fr.epf.moov

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.metroline_view.view.*

class MetroLineAdapter(val metroLines: List<MetroLine>) : RecyclerView.Adapter<MetroLineAdapter.MetroLineViewHolder>() {
    class MetroLineViewHolder(val metroLineView: View) : RecyclerView.ViewHolder(metroLineView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MetroLineViewHolder {
        val layoutInfater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInfater.inflate(R.layout.metroline_view, parent, false)
        return MetroLineViewHolder(view)
    }
    override fun getItemCount(): Int = metroLines.size

    override fun onBindViewHolder(holder: MetroLineViewHolder, position: Int) {
        val metro = metroLines[position]
        holder.metroLineView.metroLine_directions_text.text="Ligne ${metro.code} : Directions ${metro.directions}"
        /*holder.metroLineView.client_imageview.setImageResource(
            when(client.gender){
                Gender.MAN -> R.drawable.man
                Gender.WOMAN -> R.drawable.woman
            }
        )*/

        holder.metroLineView.setOnClickListener{
            Log.d("EPF", "$metro")
            //val intent = Intent(it.context, DetailClientActivity::class.java)
            //intent.putExtra("lastname", client.lastname)
            //intent.putExtra("id", position)
            //it.context.startActivity(intent)
        }
    }
}