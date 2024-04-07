package com.example.plant
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button

import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import me.relex.circleindicator.CircleIndicator3

class PlantEnrollActivity : AppCompatActivity() {
        private lateinit var homeBtn: Button
        private lateinit var viewpager: ViewPager2
        private lateinit var indicator: CircleIndicator3
        @SuppressLint("MissingInflatedId")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_enroll)
            homeBtn = findViewById(R.id.home5)
            viewpager = findViewById(R.id.viewPager2)
            indicator = findViewById(R.id.home_pannel_indicator)
            val intent = Intent(this, HomeActivity::class.java)
            homeBtn.setOnClickListener {
                startActivity(intent)
            }

            var viewPagerEnrollAdapter = ViewPagerEnrollAdapter(this)

            viewPagerEnrollAdapter.addFragment(Fragment_Enroll1())
            viewPagerEnrollAdapter.addFragment(Fragment_Enroll2())

            viewpager.apply {
                adapter = viewPagerEnrollAdapter
                registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                    }
                })
            }
            indicator.setViewPager(viewpager)
            viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    viewpager.currentItem = position
                }
            })
        }
    }
