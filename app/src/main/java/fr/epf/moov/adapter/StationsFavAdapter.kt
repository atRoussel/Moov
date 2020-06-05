package fr.epf.moov.adapter

import android.content.Context
import android.content.res.Resources
import android.graphics.Typeface
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.facebook.stetho.okhttp3.StethoInterceptor
import fr.epf.moov.R
import fr.epf.moov.data.AppDatabase
import fr.epf.moov.data.StationDao
import fr.epf.moov.model.Station
import fr.epf.moov.service.RATPService
import kotlinx.android.synthetic.main.stationfav_view.view.*
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//TODO : créer des animations de déroulement

class StationFavAdapter(val stations: List<Station>?) : RecyclerView.Adapter<StationFavAdapter.StationFavViewHolder>() {

    private lateinit var context: Context
    private var savedStationDao: StationDao? = null
    private lateinit var service: Retrofit
    private var scheduleVisible: Boolean = false
    var schedulesList: MutableList<String> = mutableListOf()
    var way = ""
    private var stringDestinations : MutableList<String>? = mutableListOf()


    class StationFavViewHolder(val stationFavView: View) : RecyclerView.ViewHolder(stationFavView)

    override fun getItemCount(): Int {
        if (stations != null) return stations.size
        return 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StationFavViewHolder {
        val layoutInflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view: View = layoutInflater.inflate(R.layout.stationfav_view, parent, false)

        context = parent.context

        val databasesaved =
            Room.databaseBuilder(context, AppDatabase::class.java, "savedStations")
                .build()

        savedStationDao = databasesaved.getStationDao()






        return StationFavViewHolder(view)
    }

    override fun onBindViewHolder(holder: StationFavViewHolder, position: Int) {
        val station = stations?.get(position)
        way = ""
        scheduleVisible = false
        schedulesList.clear()
        stringDestinations?.clear()
        stringDestinations = getListDestinations(station?.directionLine)

        val httpLoggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        service = Retrofit.Builder()
            .baseUrl("https://api-ratp.pierre-grimaud.fr/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()


        holder.stationFavView.schedules_recyclerview.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.stationFavView.schedules_recyclerview.itemAnimator = null

        holder.stationFavView.stationfav_name_textview.text = "${station?.nameStation}"
        holder.stationFavView.aller_textview.text = "${stringDestinations?.get(0)}"
        holder.stationFavView.retour_textview.text = "${stringDestinations?.get(1)}"

        var resources: Resources = context.resources

        if(station?.typeLine == "metros") {
            val drawableName: String = "m${station?.codeLine}"
            val id: Int =
                resources.getIdentifier(drawableName, "drawable", context.packageName)
            holder.stationFavView.pictogram_imageview.setImageResource(id)
        }
        if(station?.typeLine == "rers") {
            val newCode = station?.codeLine.toLowerCase()
            val drawableName: String = "m${newCode}"
            val id: Int =
                resources.getIdentifier(drawableName, "drawable", context.packageName)
            holder.stationFavView.pictogram_imageview.setImageResource(id)
        }
        if(station?.typeLine == "tramways") {
            val drawableName: String = "t${station?.codeLine}"
            val id: Int =
                resources.getIdentifier(drawableName, "drawable", context.packageName)
            holder.stationFavView.pictogram_imageview.setImageResource(id)
        }

        holder.stationFavView.fav_imageview.setImageResource(R.drawable.fav_full)

       holder.stationFavView.schedules_recyclerview.visibility = View.GONE

        runBlocking {
            schedulesList.clear()
            way = "A"
            val result = service.create(RATPService::class.java).getSchedules(
                station!!.typeLine,
                station?.codeLine,
                station?.slugStation,
                way
            )

            result.result.schedules.map {
                var schedule = it.message
                schedulesList.add(schedule)
            }

            holder.stationFavView.schedules_recyclerview.adapter =
                ScheduleAdapter(schedulesList)
        }


            holder.stationFavView.fav_imageview.setOnClickListener {
                if (station?.favoris == true) {
                    station?.favoris = false
                    runBlocking {
                        savedStationDao?.deleteStation(station?.codeLine, station?.nameStation)
                    }


                    holder.stationFavView.fav_imageview.setImageResource(R.drawable.fav_empty)
                    Toast.makeText(
                        context,
                        "La station a été supprimée des favoris",
                        Toast.LENGTH_SHORT
                    ).show()

                } else if (station?.favoris == false) {
                    station?.favoris = true

                    runBlocking {
                        savedStationDao?.addStation(station)
                    }

                    holder.stationFavView.fav_imageview.setImageResource(R.drawable.fav_full)
                    Toast.makeText(
                        context,
                        "La station a été rajoutée aux favoris",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                }
            }
            holder.stationFavView.setOnClickListener {
            if(scheduleVisible == true){
                holder.stationFavView.schedules_recyclerview.visibility = View.GONE
                holder.stationFavView.destinations_exchange.visibility = View.INVISIBLE
                holder.stationFavView.aller_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17F)
                holder.stationFavView.aller_textview.typeface = Typeface.DEFAULT
                holder.stationFavView.retour_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17F)
                holder.stationFavView.develop_imageview.rotation = 90F
                scheduleVisible = false
                }else{
                runBlocking {
                    val result =  service.create(RATPService::class.java).getSchedules(station!!.typeLine, station.codeLine, station.slugStation, way)

                    schedulesList.clear()

                    result.result.schedules.map {
                        var schedule = it.message
                        schedulesList.add(schedule)

                    }
                }
                holder.stationFavView.schedules_recyclerview.visibility = View.VISIBLE
                holder.stationFavView.destinations_exchange.visibility = View.VISIBLE
                holder.stationFavView.develop_imageview.rotation = 270F
                holder.stationFavView.aller_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18F)
                holder.stationFavView.aller_textview.typeface = Typeface.DEFAULT_BOLD
                holder.stationFavView.retour_textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13F)
                scheduleVisible = true
            }


            }


        holder.stationFavView.destinations_exchange.setOnClickListener {
            if (way == "A") {
                way = "R"
                runBlocking {
                    val result =  service.create(RATPService::class.java).getSchedules(station!!.typeLine, station.codeLine, station.slugStation, way)

                    schedulesList.clear()

                    result.result.schedules.map {
                        var schedule = it.message
                        schedulesList.add(schedule)

                    }
                }
                holder.stationFavView.schedules_recyclerview.adapter =
                    ScheduleAdapter(schedulesList)
                stringDestinations?.clear()
                stringDestinations = getListDestinations(station?.directionLine)
                holder.stationFavView.aller_textview.text = stringDestinations?.get(1)
                holder.stationFavView.retour_textview.text = stringDestinations?.get(0)
            } else if (way == "R") {
                way = "A"
                runBlocking {
                    val result =  service.create(RATPService::class.java).getSchedules(station!!.typeLine, station.codeLine, station.slugStation, way)

                    schedulesList.clear()

                    result.result.schedules.map {
                        var schedule = it.message
                        schedulesList.add(schedule)

                    }
                }
                holder.stationFavView.schedules_recyclerview.adapter =
                    ScheduleAdapter(schedulesList)
                stringDestinations?.clear()
                stringDestinations = getListDestinations(station?.directionLine)
                holder.stationFavView.aller_textview.text = stringDestinations?.get(0)
                holder.stationFavView.retour_textview.text = stringDestinations?.get(1)
            }
        }

    }


        fun getListDestinations(destinations: String?): MutableList<String>? {
            if (destinations != null) {
                return destinations.split(" / ") as MutableList<String>
            }
            return null
        }
    }

