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
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.content_main.*
import okio.internal.commonAsUtf8ToByteArray
import okio.utf8Size
import kotlin.random.Random


class MainActivity : AppCompatActivity() {

    lateinit var toolbar: Toolbar


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        window.setBackgroundDrawableResource(R.drawable.background_no_opacity)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_find,
                R.id.navigation_trip,
                R.id.navigation_saved,
                R.id.navigation_profil
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val switchView = intent.getIntExtra("switchView", 0)

        if (switchView == 1 || switchView == 2 || switchView == 3) {
            val bundle = bundleOf("switchView" to switchView)
            navController.navigate(R.id.navigation_find, bundle)
        }

    }

    }



