package com.example.plant

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class ProfileActivity : AppCompatActivity(){
    private lateinit var profileUserName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var profileBirth: TextView
    private lateinit var profileJoinDate: TextView
    private lateinit var cancelBtn: Button
    private var userItem: UserItem = UserItem()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val email = intent.getStringExtra("userEmail")
        var userName = intent.getStringExtra("userName")
        profileUserName = findViewById(R.id.profileUserName)
        profileEmail = findViewById(R.id.profileEmail)
        profileBirth = findViewById(R.id.profileBirth)
        profileJoinDate = findViewById(R.id.joinDate)

        GlobalScope.launch(Dispatchers.IO) {
            email?.let { getUserInfo(it) }
        }

        cancelBtn = findViewById(R.id.closeProfilePage)
        cancelBtn.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra("userEmail", email)
            intent.putExtra("userName", userName)
            startActivity(intent)
        }
    }

    private fun getUserInfo(userEmail: String) {
        val url = URL("http://10.0.2.2/getuserinfo.php")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true

        val postData = URLEncoder.encode("uemail", "UTF-8") + "=" + URLEncoder.encode(userEmail, "UTF-8")
        val outputStream = OutputStreamWriter(connection.outputStream)
        outputStream.write(postData)
        outputStream.flush()
        outputStream.close()

        val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
        val response = StringBuilder()
        var line: String?
        while (inputStream.readLine().also { line = it } != null) {
            response.append(line)
        }
        inputStream.close()

        val jsonResponse = JSONObject(response.toString())
        val status = jsonResponse.getString("status")
        if (status == "success") {
            val dataObject = jsonResponse.getJSONObject("data")
            // 가져온 데이터를 처리
            handleSuccess(dataObject)
        } else {
            // 실패 처리
            handleFailure()
        }
    }
    // 데이터 가져오기가 성공한 경우 처리할 로직
    private fun handleSuccess(dataObject: JSONObject) {
            //userItem = UserItem()
            val userInfoItem = UserItem().apply {
                setUid(dataObject.getString("uid"))
                setUemail(dataObject.getString("uemail"))
                setJoinDate(dataObject.getString("joindate"))
                setUbirth(dataObject.getString("ubirth"))
            }
        Log.d("MyTag", "Data retrieved successfully!")
        userItem = userInfoItem // userItem을 업데이트

        // UI 업데이트는 메인 스레드에서 실행되어야 함
        runOnUiThread {
            profileUserName.text = userItem.getUid()
            profileEmail.text = userItem.getUemail()
            profileBirth.text = userItem.getUbirth()
            profileJoinDate.text = userItem.getJoinDate()
        }

    }

    // 데이터 가져오기가 실패한 경우 처리할 로직
    private fun handleFailure() {

    }

}