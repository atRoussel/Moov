package fr.epf.moov


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast

import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.content_main.*
import okio.internal.commonAsUtf8ToByteArray
import okio.utf8Size
import kotlin.random.Random


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var navView: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val tsvReader = csvReader{
            delimiter = ';'
        }
        val astucesCsv = resources.openRawResource(R.raw.astuces)
        val listAstuces: List<List<String>> = tsvReader.readAll(astucesCsv)
        var idRandom : Int = Random.nextInt(listAstuces.size)+1

        listAstuces.map {

    if(it[0]==idRandom.toString()){
        val charset = Charsets.UTF_8
        val byteArray = it[1].toByteArray(charset)
       Log.d("Astuces",byteArray.contentToString()) // [72, 101, 108, 108, 111]
        Log.d("Astuces",byteArray.toString(charset)) // Hello
    astuce_main_textview.text = byteArray.toString(charset)
}

        }


        qrcode_button_main.setOnClickListener {
            val intent = Intent(this, QRCodeActivity::class.java)
            startActivity(intent)
        }

        
        qrcode_button_main.setOnClickListener {
            val intent = Intent(this, QRCodeActivity::class.java)
            startActivity(intent)
        }



        schedule_button_main.setOnClickListener {
            val intent = Intent(this, ScheduleActivity::class.java)
            startActivity(intent)
        }

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        drawerLayout = findViewById(R.id.drawer_layout)
        navView = findViewById(R.id.nav_view)

        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, 0, 0
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)





    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_favoris -> {
                val intent = Intent(this, FavorisActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_maps -> {
                val intent = Intent(this, MapsActivity::class.java)
                startActivity(intent)
            }
            R.id.nav_lists -> {
                val intent = Intent(this, ListesActivity::class.java)
                startActivity(intent)
            }
           /* R.id.nav_update -> {
                Toast.makeText(this, "Update clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_logout -> {
                Toast.makeText(this, "Sign out clicked", Toast.LENGTH_SHORT).show()
            }*/
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}
