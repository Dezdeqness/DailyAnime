package com.dezdeqness.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dezdeqness.R
import com.dezdeqness.databinding.ActivityMainBinding
import com.dezdeqness.extensions.setupWithNavController

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            setupBottomNavigationBar()
        } // Else, need to wait for onRestoreInstanceState

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        setupBottomNavigationBar()
    }

    private fun setupBottomNavigationBar() {
        val bottomVav = binding.navigation

        val navGraphIds = listOf(
            R.navigation.personal_list_host_nav_graph,
            R.navigation.calendar_nav_graph,
            R.navigation.search_nav_graph,
            R.navigation.profile_nav_graph,
        )

         bottomVav.setupWithNavController(
            navGraphIds = navGraphIds,
            fragmentManager = supportFragmentManager,
            containerId = R.id.container,
            intent = intent
        )

    }

}
