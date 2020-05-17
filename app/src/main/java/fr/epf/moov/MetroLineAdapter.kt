package fr.epf.moov

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.epf.moov.model.MetroLine
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
        holder.metroLineView.metroLine_imageview.setImageResource(
            when(metro.code){
                "1" -> R.drawable.m1
                "2" -> R.drawable.m2
                "3" -> R.drawable.m3
                "3b" -> R.drawable.m3bis
                "4" -> R.drawable.m4
                "5" -> R.drawable.m5
                "6" -> R.drawable.m6
                "7" -> R.drawable.m7
                "7b" -> R.drawable.m7bis
                "8" -> R.drawable.m8
                "9" -> R.drawable.m9
                "10" -> R.drawable.m10
                "11" -> R.drawable.m11
                "12" -> R.drawable.m12
                "13" -> R.drawable.m13
                "14" -> R.drawable.m14

                else -> R.drawable.m14
            }
        )

        holder.metroLineView.setOnClickListener{
            val intent = Intent(it.context, DetailLineActivity::class.java)
            intent.putExtra("directions", metro.directions)
            intent.putExtra("code", metro.code)
            it.context.startActivity(intent)
        }
    }
}