package com.example.plant

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity(){
    private lateinit var Fragment_Board: Fragment_Board
    private lateinit var Fragment_Home: Fragment_Home
    private lateinit var Fragment_Garden: Fragment_Garden
    private lateinit var Fragment_Search: Fragment_Search
    private lateinit var bottomNavigationView:BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        Fragment_Home = Fragment_Home()
        Fragment_Board = Fragment_Board()
        Fragment_Garden = Fragment_Garden()
        Fragment_Search = Fragment_Search()
        supportFragmentManager.beginTransaction().replace(R.id.container, Fragment_Home).commit();

        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.tab1 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, Fragment_Home).commit()
                    true
                }
                R.id.tab2 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, Fragment_Board).commit()
                    true
                }
                R.id.tab3 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, Fragment_Garden).commit()
                    true
                }
                R.id.tab4 -> {
                    supportFragmentManager.beginTransaction().replace(R.id.container, Fragment_Search).commit()
                    true
                }
                else -> false
            }
        }
    }
}