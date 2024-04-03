package com.example.plant

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
const val GALLERY_REQUEST_CODE = 1001

class DiaryActivity: AppCompatActivity() {
    private lateinit var image:ImageView
    private lateinit var date:TextView
    private lateinit var beforeBtn:Button
    private lateinit var saveBtn:Button
    private lateinit var homeBtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val choiceDate = intent.getStringExtra("date")
        image = findViewById(R.id.imageView)
        date = findViewById(R.id.date2)
        beforeBtn = findViewById(R.id.beforBtn)
        saveBtn = findViewById(R.id.saveBtn)
        homeBtn = findViewById(R.id.home3)

        date.text = choiceDate
        beforeBtn.setOnClickListener {
            val intent = Intent(this, CalenderActivity::class.java)
            startActivity(intent)
        }
        image.setOnClickListener {
            // 갤러리로 접근하기 위한 Intent 생성
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*" // 이미지 타입으로 필터링

            // 갤러리 앱을 실행하고 이미지를 선택할 수 있도록 요청
            startActivityForResult(intent, GALLERY_REQUEST_CODE)
        }
        homeBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }


    }
    // onActivityResult 메서드를 오버라이드하여 갤러리로부터 선택된 이미지를 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // 갤러리에서 선택한 이미지의 URI 가져오기
            val selectedImageUri: Uri? = data?.data

            // 선택한 이미지를 ImageView에 표시
            selectedImageUri?.let { uri ->
                image.setImageURI(uri)
            }
        }
    }
}