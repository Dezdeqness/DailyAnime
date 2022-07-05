package com.dezdeqness.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dezdeqness.R
import com.dezdeqness.core.TrapFragment
import com.dezdeqness.databinding.ActivityMainBinding
import com.dezdeqness.presentation.features.animelist.ListAnimeFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val listFragment = ListAnimeFragment()

        binding.navigation.setOnItemSelectedListener { menuItem ->
            val fragment = when (menuItem.itemId) {
                R.id.user_preferences,
                R.id.calendar,
                R.id.current,
                R.id.profile -> {
                    TrapFragment()
                }
                R.id.search -> {
                    listFragment
                }
                else -> null
            }
            fragment?.let {
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.container, fragment)
                    commit()
                }
            }
            true
        }
    }
}
