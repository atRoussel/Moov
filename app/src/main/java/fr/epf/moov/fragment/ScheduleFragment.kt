package fr.epf.moov.fragment

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import fr.epf.moov.R
import fr.epf.moov.adapter.ScheduleAdapter
import fr.epf.moov.data.AppDatabase
import fr.epf.moov.data.StationDao
import fr.epf.moov.model.Station
import fr.epf.moov.service.RATPService
import fr.epf.moov.service.retrofit
import kotlinx.android.synthetic.main.activity_horaires.*
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.coroutines.runBlocking

class ScheduleFragment : Fragment(){

    var schedulesList: MutableList<String> = mutableListOf()
    var allStations: List<Station>? = null
    var favStations: List<Station>? = mutableListOf()
    var allStationsName: MutableList<String> = mutableListOf()
    private lateinit var station: Station
    private var stationDao: StationDao? = null
    private var savedStationDao: StationDao? = null
    val service = retrofit().create(RATPService::class.java)
    var listDestinations: List<String>? = null
    var stringDestinations:String = ""
    var listAstuces: MutableList<String>? = mutableListOf()
    var way: String = "A"
    var url: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_schedule, container,false)
        var schedules_recyclerview = view.findViewById<RecyclerView>(R.id.schedules_recyclerview)
        var  autoComplete_stations = view.findViewById<AutoCompleteTextView>(R.id. autoComplete_stations)
        val buttonSchedule = view.findViewById<ImageButton>(R.id.schedule_button)
        val nameStation = view.findViewById<TextView>(R.id. station_name_textview)
        val  aller = view.findViewById<TextView>(R.id. aller_textview)
        val retour = view.findViewById<TextView>(R.id.retour_textview)
        val pictogram = view.findViewById<ImageView>(R.id.pictogram_imageview)
        val favoriImage = view.findViewById<ImageView>(R.id.fav_imageview)
        val scheduleLayout = view.findViewById<CardView>(R.id.global_schedule_layout)
        val exchangeButton = view.findViewById<ImageButton>(R.id.destinations_exchange)
        var astuceTextView = view.findViewById<TextView>(R.id.astuce_textview)

        initAstuce()
        val index = (0..9).random()

        astuceTextView.text = listAstuces?.get(index)

        schedules_recyclerview.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        val database =
            Room.databaseBuilder(requireContext(), AppDatabase::class.java, "listStations")
                .build()
        stationDao = database.getStationDao()
        val databasesaved =
            Room.databaseBuilder(requireContext(), AppDatabase::class.java, "savedStations")
                .build()
        savedStationDao = databasesaved.getStationDao()

        //Récupération de toutes les stations
        runBlocking {
            allStations = stationDao?.getStations()
            allStations?.map {
                allStationsName.add("${it.nameStation} (Ligne ${it.codeLine})")
            }
        }


        //Récupération des stations favoris
        runBlocking {
            favStations = savedStationDao?.getStations()
        }

        val adapter: ArrayAdapter<String> =
            ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, allStationsName)
        autoComplete_stations.setAdapter(adapter)

        buttonSchedule.setOnClickListener {
            hideKeyboard()
            val autoComplete_text = autoComplete_stations.text.toString()
            val stationName = autoComplete_text.substringBefore(" (Ligne")
            val codeLine = autoComplete_text.substringAfter("(Ligne ").substringBefore(")")
            var slug = ""
            var type = ""
            var code = ""
            var favoris = false
            allStations?.forEach {
                if (it.nameStation == stationName && it.codeLine == codeLine) {
                    slug = it.slugStation
                    type = it.typeLine
                    code = it.codeLine
                    favoris = getFavoris(stationName, code)
                    runBlocking {
                        val result = service.getStation(type, code)
                        stringDestinations = result.result.directions
                        listDestinations = getListDestinations(stringDestinations)
                    }
                }
            }
            station = Station(
                0,
                type,
                code,
                stationName,
                slug,
                stringDestinations,
                favoris
            )
            runBlocking {
                val result = service.getSchedules(type, code, slug, way)
                schedulesList.clear()
                result.result.schedules.map{
                    var schedule = it.message
                    schedulesList.add(schedule)

                }
            }
            Log.d("OU", schedulesList.toString())


            schedules_recyclerview.adapter =
                ScheduleAdapter(schedulesList)

            nameStation.text = stationName
            aller.text = listDestinations?.get(0)
            retour.text = listDestinations?.get(1)
            val cityCsv = resources.openRawResource(R.raw.pictogrammes)
            val listPictogrammes: List<List<String>> = csvReader().readAll(cityCsv)
            var resources: Resources = this.resources

            if(type == "metros") {
                val drawableName: String = "m${code}"
                val id: Int =
                    resources.getIdentifier(drawableName, "drawable", requireActivity().packageName)
                pictogram.setImageResource(id)
            }
            if(type == "rers") {
                val newCode = code.toLowerCase()
                val drawableName: String = "m${newCode}"
                val id: Int =
                    resources.getIdentifier(drawableName, "drawable", requireActivity().packageName)
                pictogram.setImageResource(id)
            }
            if(type == "tramways") {
                val drawableName: String = "t${code}"
                val id: Int =
                    resources.getIdentifier(drawableName, "drawable", requireActivity().packageName)
                pictogram.setImageResource(id)
            }

            if (station.favoris == true)
                favoriImage.setImageResource(R.drawable.fav_full)
            scheduleLayout.visibility = View.VISIBLE
            favoriImage.setOnClickListener {
                if (station.favoris == true) {
                    station.favoris = false
                    favoriImage.setImageResource(R.drawable.fav_empty)
                    runBlocking {
                        savedStationDao?.deleteStation(station.id)
                    }
                    Toast.makeText(
                        requireContext(),
                        "La station a été supprimée des favoris",
                        Toast.LENGTH_SHORT
                    ).show()
                } else if (station.favoris == false) {
                    station.favoris = true
                    favoriImage.setImageResource(R.drawable.fav_full)
                    runBlocking {
                        savedStationDao?.addStation(station)
                    }
                    Toast.makeText(requireContext(), "La station a été ajoutée aux favoris", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            exchangeButton.setOnClickListener {
                if (way == "A") {
                    way = "R"
                    runBlocking {
                        val result = service.getSchedules(type, code, slug, way)
                        schedulesList.clear()
                        result.result.schedules.map {
                            var schedule = it.message
                            schedulesList.add(schedule)
                        }
                    }
                    schedules_recyclerview.adapter =
                        ScheduleAdapter(schedulesList)
                    aller.text = listDestinations?.get(1)
                    retour.text = listDestinations?.get(0)
                } else if (way == "R") {
                    way = "A"
                    runBlocking {
                        val result = service.getSchedules(type, code, slug, way)
                        schedulesList.clear()
                        result.result.schedules.map {
                            var schedule = it.message
                            schedulesList.add(schedule)
                        }
                    }
                    schedules_recyclerview.adapter =
                        ScheduleAdapter(schedulesList)
                    aller.text = listDestinations?.get(0)
                    retour.text = listDestinations?.get(1)
                }
            }
        }


        return view
    }


    fun getListDestinations(destinations: String?): List<String>? {
        if (destinations != null) {
            return destinations.split(" / ")
        }
        return null
    }

    private fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
    fun getFavoris(stationName: String, codeLine: String): Boolean {
        var favoris = false
        favStations?.forEach {
            if (stationName == it.nameStation && codeLine == it.codeLine)
                favoris = true
        }
        return favoris
    }


    fun initAstuce(){
        listAstuces?.add("De 1900 à 1992 il y avait une 1ère classe et une 2nd classe dans les métros pour séparer la haute société du reste de la population.")
        listAstuces?.add("La station la plus profondément enfouie est Abbesses (ligne 12) qui se trouve 36 mètres sous le sol.")
        listAstuces?.add("Châtelet - Les Halles est la plus grande station de métro au monde. Si les piliers et les affichages vous font zigzaguer et vous perdre c'est voulu : le but est de casser les grands mouvements de foule.")
        listAstuces?.add("Il était courant de se faire insulter par le poinçonneur et les autres passagers si on tendait son ticket dans le mauvais sens ou qu'on le faisait tomber à l'entrée du quai, en retardant tout le monde.")
        listAstuces?.add("Il existe pas mal de stations fantômes au sein de la capitale comme Haxo, une station entre la 3bis et la 7bis dont on n'a jamais percé les accès en surface car annulée avant sa mise en service.")
        listAstuces?.add("Si vous voyez une scène de film dans le métro parisien, elle a surement tournée à Porte des Lilas Cinéma qui depuis sa fermeture ne sert qu'à ça. On y a notamment tourné Amélie Poulain.")
        listAstuces?.add("Les stations Rue d'Allemagne et Berlin sont devenues Jaurès et Liège en 1914 après l'assassinat de Jaurès en 1914 et l'éclatement de la première Guerre Mondiale.")
        listAstuces?.add("Pour construire la ligne 1, toute la rue de Rivoli a été éventrée pour une construction à cœur ouvert, suspendant ainsi tout trafic pendant les travaux.")
        listAstuces?.add("Contrairement à la pensée générale, Gare du Nord est la station la plus fréquentée quotidiennement. Châtelet-les-Halles arrive en deuxième position.")
        listAstuces?.add("Le lapin de la RATP qui se fait pincer très fort sur les portes du métro s'appelle Serge, c'est la mascotte de la RATP depuis 31 ans.")


    }


}