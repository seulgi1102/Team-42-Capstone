package com.example.plant

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import java.util.regex.Pattern

class MainActivity : AppCompatActivity() {
    private lateinit var id: EditText
    private lateinit var password: EditText
    private lateinit var signUp: TextView
    private lateinit var signInBtn: Button
    private lateinit var alarmId: TextView
    private lateinit var alarmPwd: TextView
    private val pattern = android.util.Patterns.EMAIL_ADDRESS

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        id = findViewById(R.id.id)
        password = findViewById(R.id.password)
        signUp = findViewById(R.id.signUp)
        signInBtn = findViewById(R.id.signInBtn)
        alarmId = findViewById(R.id.alarmId)
        alarmPwd = findViewById(R.id.alarmPwd)

        val intent = Intent(this, SignUpActivity::class.java)
        val intent2 = Intent(this, HomeActivity::class.java)
        signInBtn.setOnClickListener {
            startActivity(intent2)
        }
        id.addTextChangedListener {
            var id = id.text.toString()
            if (id.isNotEmpty()) {
                if (pattern.matcher(id).matches()) {
                    alarmId.visibility = View.GONE
                } else {
                    alarmId.visibility = View.VISIBLE
                }
            } else {
                alarmId.visibility = View.GONE
            }
        }
        password.addTextChangedListener {
            var pwd = password.text.toString()
            if (pwd.isNotEmpty()) {
                checkValidation(pwd, alarmPwd)
            } else {
                alarmPwd.visibility = View.GONE
            }
        }
        signUp.setOnClickListener {
            startActivity(intent)
        }
    }

    companion object {
        fun checkValidation(password: String, alarmPwd: TextView) {
            // 비밀번호 유효성 검사식1 : 숫자, 특수문자가 포함되어야 한다.
            val symbol = "([0-9].*[!,@,#,^,&,*,(,)])|([!,@,#,^,&,*,(,)].*[0-9])"
            // 비밀번호 유효성 검사식2 : 영문자 대소문자가 적어도 하나씩은 포함되어야 한다.
            val alpha = "(?=.*[a-zA-Z])"
            // 정규표현식 컴파일
            val length = 10
            val patternSymbol = Pattern.compile(symbol)
            val patternAlpha = Pattern.compile(alpha)

            val matcherSymbol = patternSymbol.matcher(password)
            val matcherAlpha = patternAlpha.matcher(password)

            if (matcherSymbol.find() && matcherAlpha.find() && password.length >= length) {
                alarmPwd.visibility = View.GONE
            } else {
                alarmPwd.visibility = View.VISIBLE
            }

        }
    }
}