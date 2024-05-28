package com.example.plant

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

class Fragment_Album2 : Fragment() {
    private var userEmail: String? = null
    private var plantName: String? = null
    private var plantId: Int = 0
    private var plantDate: String? = null
    private var plantPoint: String? = null
    private var plantHour: Int = 0
    private var plantMinute: Int = 0
    private var plantPlace: String? = null
    private var wateringCycle: String? = null
    private var imageUrl: String? = null
    private var wateringAlarm: Int = 0
    private var tempHumidAlarm: Int = 0
    private var temperature: String? = null
    private var humid: String? = null
    private var enrollTime: String = ""
    private lateinit var date: TextView
    private lateinit var place: TextView
    private lateinit var pname: TextView
    private lateinit var phumid: TextView
    private lateinit var ptemp: TextView
    private lateinit var pwateringCycle: TextView
    private lateinit var ptime: TextView
    private lateinit var image: ImageView
    private lateinit var pwateringAlarm:TextView
    private lateinit var ptempHumidAlarm:TextView
    private lateinit var point: TextView
    private lateinit var beforeBtn: ImageView
    private lateinit var editBtn:FloatingActionButton
    private lateinit var deleteBtn: FloatingActionButton

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_album2, container, false)
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.GONE
        arguments?.let {
            userEmail = it.getString("userEmail")
            plantName = it.getString("plantName")
            plantId = it.getInt("plantId")
            plantDate = it.getString("plantDate")
            plantPoint = it.getString("plantPoint")
            plantHour = it.getInt("plantHour")
            plantMinute = it.getInt("plantMinute")
            plantPlace = it.getString("plantPlace")
            wateringCycle = it.getString("wateringCycle")
            imageUrl = it.getString("imageUrl")
            wateringAlarm = it.getInt("wateringAlarm")
            tempHumidAlarm = it.getInt("tempHumidAlarm")
            temperature = it.getString("temperature")
            enrollTime = it.getString("enrollTime").toString()
            humid = it.getString("humid")
        }
        date = view.findViewById(R.id.date)
        pname = view.findViewById(R.id.detailName)
        place = view.findViewById(R.id.place)
        phumid = view.findViewById(R.id.humid)
        ptemp = view.findViewById(R.id.temp)
        point = view.findViewById(R.id.point)
        ptime = view.findViewById(R.id.wateringTime)
        pwateringCycle = view.findViewById(R.id.watering)
        pwateringAlarm =view.findViewById(R.id.wateringAlarm)
        image = view.findViewById(R.id.detailImageView)
        ptempHumidAlarm =view.findViewById(R.id.tempHumidAlarm)
        beforeBtn = view.findViewById(R.id.beforeBtn)
        editBtn = view.findViewById(R.id.editBtn)
        deleteBtn = view.findViewById(R.id.deleteBtn)

        if(wateringAlarm == 0){
            pwateringAlarm.text = "OFF"
        }else{
            pwateringAlarm.text = "ON"
        }
        if(tempHumidAlarm == 0){
            ptempHumidAlarm.text = "OFF"
        }else{
            ptempHumidAlarm.text = "ON"
        }

        pname.text = plantName
        place.text = plantPlace
        date.text = plantDate
        point.text = plantPoint
        phumid.text = humid
        ptemp.text = temperature
        if(wateringCycle == ""){
            pwateringCycle.setTextColor(Color.parseColor("#ACACAC"))
            pwateringCycle.text = "선택된 요일이 없습니다."
        }else{
            pwateringCycle.text = wateringCycle
        }
        ptime.text = plantHour.toString() + ":" + plantMinute.toString()
        Glide.with(requireContext())
            .load(imageUrl) // 이미지 URL
            .into(image) // 이미지뷰에 로드된 이미지 설정
        beforeBtn.setOnClickListener {
            replaceFragment(Fragment_Album())
        }
        deleteBtn.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("플랜텀")
                .setMessage("정말 삭제하시겠습니까?")
                .setPositiveButton("삭제") { dialog, _ ->
                    deletePlant(plantId)
                    dialog.dismiss() // 다이얼로그 닫기
                }
                .show()
        }
        editBtn.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()

            val fragment = Fragment_Album3()
            val bundle = Bundle()

            // Bundle에 데이터를 담기
            bundle.putString("userEmail", userEmail)
            bundle.putString("plantName", plantName)
            bundle.putInt("plantId", plantId)
            bundle.putString("plantDate", plantDate)
            bundle.putString("plantPoint", plantPoint)
            bundle.putInt("plantHour", plantHour)
            bundle.putInt("plantMinute", plantMinute)
            bundle.putString("plantPlace", plantPlace)
            bundle.putString("wateringCycle", wateringCycle)
            bundle.putString("imageUrl", imageUrl)
            bundle.putInt("wateringAlarm", wateringAlarm)
            bundle.putInt("tempHumidAlarm", tempHumidAlarm)
            bundle.putString("temperature", temperature)
            bundle.putString("enrollTime", enrollTime)
            bundle.putString("humid", humid)
            // Fragment에 Bundle을 설정
            fragment.arguments = bundle

            // FragmentTransaction을 사용하여 PlantEnrollFragment로 전환
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // 이전 Fragment로 돌아갈 수 있도록 back stack에 추가
            transaction.commit() // 변경 사항을 적용
        }
        return view
    }
    private fun deletePlant(plantid: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // PHP 스크립트의 URL
                val url = URL("http://192.168.233.22:80/deleteplant.php")

                // HttpURLConnection 열기
                val connection = url.openConnection() as HttpURLConnection

                // POST 요청 설정
                connection.requestMethod = "POST"
                connection.doOutput = true

                // POST 데이터 작성
                val postData = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(plantid.toString(), "UTF-8")
                // 데이터 전송
                val outputStream = OutputStreamWriter(connection.outputStream)
                outputStream.write(postData)
                outputStream.flush()
                outputStream.close()

                // 응답 처리
                val inputStream: BufferedReader =
                    if (connection.responseCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader(InputStreamReader(connection.inputStream))
                    } else {
                        BufferedReader(InputStreamReader(connection.errorStream))
                    }

                val response = StringBuilder()
                var line: String?
                while (inputStream.readLine().also { line = it } != null) {
                    response.append(line)
                }
                inputStream.close()

                val result = response.toString()
                Log.d("ServerResponse", result)
                // 서버로부터의 응답에 따라 처리
                if (result == "delete successful") {
                    launch(Dispatchers.Main) {
                        replaceFragment(Fragment_Album())//Fragment_Diary1 으로 넘어감
                    }
                } else {
                    launch(Dispatchers.Main) {
                        AlertDialog.Builder(requireContext())
                            .setTitle("플랜텀")
                            .setMessage("삭제 실패")
                            .setPositiveButton("확인") { dialog, which -> Log.d("MyTag", "positive") }
                            .create()
                            .show()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                launch(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "catch", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun replaceFragment(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()

        val fragment = fragment
        val bundle = Bundle()

        bundle.putString("userEmail", userEmail)


        // Fragment에 Bundle을 설정
        fragment.arguments = bundle

        // FragmentTransaction을 사용하여 PlantEnrollFragment로 전환
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null) // 이전 Fragment로 돌아갈 수 있도록 back stack에 추가
        transaction.commit() // 변경 사항을 적용
    }
}