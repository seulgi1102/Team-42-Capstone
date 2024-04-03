package com.example.plant

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity(){
    private lateinit var diary: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        diary = findViewById(R.id.diaryBtn)
        val intent = Intent(this,CalenderActivity::class.java)
        diary.setOnClickListener {
            startActivity(intent)
        }
    }
}