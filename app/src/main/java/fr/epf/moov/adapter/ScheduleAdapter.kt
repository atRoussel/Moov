package fr.epf.moov.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.epf.moov.R
import kotlinx.android.synthetic.main.schedule_view.view.*

class ScheduleAdapter (val schedules : MutableList<String>?) : RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>(){

    class ScheduleViewHolder(val scheduleView : View) : RecyclerView.ViewHolder(scheduleView)

    override fun getItemCount(): Int {
        if (schedules == null){
            return 0
        }else{
            return schedules!!.size
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.schedule_view, parent, false)

        return ScheduleViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {


        val schedule = schedules?.get(position)
        holder.scheduleView.schedule_textview.text = "${schedule}"

    }


}