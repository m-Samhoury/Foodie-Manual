package com.moustafa.foodiemanual.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.moustafa.foodiemanual.R

/**
 * @author moustafasamhoury
 * created on Sunday, 17 Nov, 2019
 */

class MainActivity : AppCompatActivity(R.layout.activity_main) {

    private val appBarConfiguration = AppBarConfiguration(setOf(R.id.restaurantsListFragment))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return

        setupActionBarWithNavController(
            navController = host.navController,
            configuration = appBarConfiguration
        )
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration)
    }

}
