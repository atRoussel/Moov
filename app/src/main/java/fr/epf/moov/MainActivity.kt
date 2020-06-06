package fr.epf.moov

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        window.setBackgroundDrawableResource(R.drawable.background_transparent)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_schedule,
                R.id.navigation_qrcode,
                R.id.navigation_map,
                R.id.navigation_saved,
                R.id.navigation_list
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val switchView = intent.getIntExtra("switchView", 0)

        if (switchView == 1 || switchView == 2 || switchView == 3) {
            val bundle = bundleOf("switchView" to switchView)
            navController.navigate(R.id.navigation_qrcode, bundle)
        }

    }

    }



