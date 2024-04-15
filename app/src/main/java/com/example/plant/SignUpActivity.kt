package com.example.plant

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class SignUpActivity : AppCompatActivity() {
    private lateinit var id: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var confirmPwd: EditText
    private lateinit var alarmPwd2: TextView
    private lateinit var alarmPwd: TextView
    private lateinit var alarmEmail: TextView
    private lateinit var login:TextView
    private lateinit var signupbtn: Button
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
        signupbtn = findViewById(R.id.signUpBtn)

        val intent = Intent(this, MainActivity::class.java)

        login.setOnClickListener {
            startActivity(intent)
        }

        signupbtn.setOnClickListener {
            val uid = id.text.toString()
            val upw = password.text.toString()
            val uemail = email.text.toString()

            GlobalScope.launch(Dispatchers.IO) {
                signup(uid, upw, uemail)
            }
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

    private fun signup(uid:String, upw:String, uemail:String) {
        try {
            val url = URL("http://10.0.2.2/signup.php")

            // HttpURLConnection 열기
            val connection = url.openConnection() as HttpURLConnection

            // POST 요청 설정
            connection.requestMethod = "POST"
            connection.doOutput = true

            // 데이터 작성
            var postData = URLEncoder.encode("uid", "UTF-8") + "=" + URLEncoder.encode(uid, "UTF-8")
            postData += "&" + URLEncoder.encode("upw", "UTF-8") + "=" + URLEncoder.encode(upw, "UTF-8")
            postData += "&" + URLEncoder.encode("uemail", "UTF-8") + "=" + URLEncoder.encode(uemail, "UTF-8")
            val outputStream = OutputStreamWriter(connection.outputStream)
            outputStream.write(postData)
            outputStream.flush()

            // 응답 코드 확인
            val intent = Intent(this, MainActivity::class.java)
            val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 성공적으로 요청이 처리됨
                runOnUiThread {
                    AlertDialog.Builder(this)
                        .setTitle("With P")
                        .setMessage("회원가입 되었습니다.")
                        .setPositiveButton("확인"
                        ) { dialog, which -> startActivity(intent) }
                        .create()
                        .show()
                }
            } else {
                // 요청이 실패한 경우
                runOnUiThread {
                    AlertDialog.Builder(this)
                        .setTitle("With P")
                        .setMessage("회원가입 실패했습니다.")
                        .setPositiveButton("확인"
                        ) { dialog, which -> Log.d("MyTag", "positive") }
                        .create()
                        .show()
                }
            }

            // 연결 종료
            connection.disconnect()
        } catch (e: Exception) {
            // 예외 처리
            e.printStackTrace()
            runOnUiThread {
                AlertDialog.Builder(this)
                    .setTitle("With P")
                    .setMessage("회원가입 실패했습니다.")
                    .setPositiveButton("확인"
                    ) { dialog, which -> Log.d("MyTag", "positive") }
                    .create()
                    .show()
            }
        }
    }
}