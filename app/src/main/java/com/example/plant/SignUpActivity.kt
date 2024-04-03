package com.example.plant

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener

class SignUpActivity : AppCompatActivity() {
    private lateinit var id: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPwd: EditText
    private lateinit var alarmPwd2: TextView
    private lateinit var alarmPwd: TextView
    private lateinit var alarmEmail: TextView
    private lateinit var login:TextView
    private val pattern = android.util.Patterns.EMAIL_ADDRESS

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        id = findViewById(R.id.nickName)
        email = findViewById(R.id.email)
        password = findViewById(R.id.signUpPassword)
        confirmPwd = findViewById(R.id.confirmPassword)
        alarmPwd2 = findViewById(R.id.alarmPwd3)
        alarmPwd = findViewById(R.id.alarmPwd2)
        alarmEmail = findViewById(R.id.alarmEmail)
        login = findViewById(R.id.goToLogin)

        val intent = Intent(this, MainActivity::class.java)

        login.setOnClickListener {
            startActivity(intent)
        }
        password.addTextChangedListener {

            var password = password.text.toString()
            if (password.isNotEmpty()) {
                MainActivity.checkValidation(password, alarmPwd)
            }
            else{
                alarmPwd.visibility = View.GONE
            }
        }
        password.addTextChangedListener { newPassword ->
            val passwordText = newPassword.toString()
            val confirmPwdText = confirmPwd.text.toString()
            if (confirmPwdText.isNotEmpty() && passwordText.isNotEmpty()) {
                if (confirmPwdText == passwordText) {
                    alarmPwd2.text = "비밀번호가 일치합니다."
                    alarmPwd2.visibility = View.VISIBLE
                } else {
                    alarmPwd2.text = "비밀번호가 불일치합니다."
                    alarmPwd2.visibility = View.VISIBLE
                }
            } else {
                alarmPwd2.visibility = View.GONE
            }
        }

        confirmPwd.addTextChangedListener { newConfirmPwd ->
            val passwordText = password.text.toString()
            val confirmPwdText = newConfirmPwd.toString()
            if (passwordText.isNotEmpty() && confirmPwdText.isNotEmpty()) {
                if (confirmPwdText == passwordText) {
                    alarmPwd2.text = "비밀번호가 일치합니다."
                    alarmPwd2.visibility = View.VISIBLE
                } else {
                    alarmPwd2.text = "비밀번호가 불일치합니다."
                    alarmPwd2.visibility = View.VISIBLE
                }
            } else {
                alarmPwd2.visibility = View.GONE
            }
        }
        email.addTextChangedListener {
            var email = email.text.toString()
            if (email.isNotEmpty()) {
                if (pattern.matcher(email).matches()) {
                    alarmEmail.visibility = View.GONE
                } else {
                    alarmEmail.text="이메일 형식이 올바르지 않습니다."
                    alarmEmail.visibility = View.VISIBLE
                }
            } else {
                alarmEmail.visibility = View.GONE
            }
        }

    }
}