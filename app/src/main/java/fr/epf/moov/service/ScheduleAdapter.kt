package fr.epf.moov.service

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.epf.moov.R
import kotlinx.android.synthetic.main.schedule_view.view.*

class ScheduleAdapter (val parameters : MutableList<String>, val schedules : MutableList<MutableList<String>>) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>(){

    class ScheduleViewHolder(val scheduleView : View) : RecyclerView.ViewHolder(scheduleView)

    override fun getItemCount(): Int = schedules.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.schedule_view, parent, false)

        return ScheduleViewHolder(view) }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {

        holder.scheduleView.station_name_textview.text = parameters[1]
        holder.scheduleView.code_metro_textview.text = parameters[0]

        val schedule = schedules[position]
        holder.scheduleView.destination_textview.text = "${schedule[0]}"
        holder.scheduleView.schedule_textview.text = "${schedule[1]}"

    }


}