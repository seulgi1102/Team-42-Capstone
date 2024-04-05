package com.example.plant

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CalendarView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class CalenderActivity : AppCompatActivity() {
    private lateinit var calender: CalendarView
    private lateinit var date: TextView
    private lateinit var nextBtn: Button
    private lateinit var homeBtn: Button
    private var choiceDate: String = ""
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calender)
        calender = findViewById(R.id.calendarView)
        date = findViewById(R.id.date)
        homeBtn = findViewById(R.id.home2)
        nextBtn = findViewById(R.id.nextBtn)
        choiceDate = ""
        //작성기록이 없으면 [작성하기]버튼이 활성화
        //기록이 있으면 달력 밑에 작성일지들의 요약버전이 리스트로 표시 그중 하나를 선택하여 상세보기 화면으로 이동
        // 상세보기 페이지에서 수정도 가능
        val intent = Intent(this, HomeActivity::class.java)

        homeBtn.setOnClickListener {
            startActivity(intent)
        }
        nextBtn.setOnClickListener {
            val intent2 = Intent(this, DiaryActivity::class.java)
            intent2.putExtra("date", choiceDate)
            startActivity(intent2)
        }
        calender.setOnDateChangeListener { view, year, month, dayOfMonth ->
            date.visibility = View.VISIBLE
            choiceDate = String.format("%d / %d / %d", year, month + 1, dayOfMonth)
            date.text = choiceDate

        }
    }
}