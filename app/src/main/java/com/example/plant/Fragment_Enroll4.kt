package com.example.plant

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
class Fragment_Enroll4 : Fragment() {
    private var userEmail: String? = null
    private lateinit var enrollBtn: Button
    private lateinit var viewModel: PlantEnrollViewModel
    /*private var pname: String = "1"
    private var pdate: String = "1"
    private var ppoint: String = ""
    private var plocation: String = ""
    private var pcycle: String = ""
    private var phour: Int = 0
    private var pminute: Int = 0
    private var ptemp: String = ""
    private var phumid: String = ""


    override fun onNameReceived(plantName: String) {
        pname = plantName
    }

    override fun onPlantInfoReceived(
        registrationDate: String,
        characteristics: String,
        location: String
    ) {
        pdate = registrationDate
        ppoint = characteristics
        plocation = location
    }

    override fun onWateringInfoReceived(
        wateringHour: Int,
        wateringMinute: Int,
        lastWateringDate: String,
        temperature: String,
        humidity: String
    ) {
        phour = wateringHour
        pminute = wateringMinute
        pcycle = lastWateringDate
        ptemp = temperature
        phumid = humidity
    }
*/
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_enroll4, container, false)

        enrollBtn = view.findViewById(R.id.enrollConfirm)
        viewModel = ViewModelProvider(requireActivity()).get(PlantEnrollViewModel::class.java)
        //로그인
        arguments?.let {
            userEmail = it.getString("userEmail")
        }

        enrollBtn.setOnClickListener {

            val uemail = userEmail
            val pnameText = viewModel.pname
            val pdateText = viewModel.pdate
            val ppointText = viewModel.ppoint
            val plocationText = viewModel.plocation
            val pcycleText = viewModel.pcycle
            val phourValue = viewModel.phour
            val pminuteValue = viewModel.pminute
            val ptempText = viewModel.ptemp
            val phumidText = viewModel.phumid
            val ptemp_alarm = viewModel.pwatering_alarm
            val phumid_alarm = viewModel.phumid_alarm

            if (pnameText != "") {
                if (ptemp_alarm == 1) {
                    if (pcycleText != "" && phourValue != 0 && pminuteValue != 0) {
                        GlobalScope.launch(Dispatchers.IO) {
                            enroll(
                                uemail,
                                pnameText,
                                pdateText,
                                ppointText,
                                plocationText,
                                pcycleText,
                                phourValue,
                                pminuteValue,
                                ptempText,
                                phumidText,
                                ptemp_alarm,
                                phumid_alarm
                            )
                        }
                    } else {
                        Toast.makeText(requireContext(), "Please fill in all required fields", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("pname", pnameText)
                    Log.d("pdate", pdateText)
                    Log.d("ppoint", ppointText)
                    Log.d("plocation", plocationText)
                    Log.d("pcycle", pcycleText)
                    GlobalScope.launch(Dispatchers.IO) {
                        enroll(
                            uemail,
                            pnameText,
                            pdateText,
                            ppointText,
                            plocationText,
                            pcycleText,
                            phourValue,
                            pminuteValue,
                            ptempText,
                            phumidText,
                            ptemp_alarm,
                            phumid_alarm
                        )
                    }
                }
            } else {
                Toast.makeText(requireContext(), "Plant name is missing", Toast.LENGTH_SHORT).show()
            }
            //startActivity(intent2)
        }
        return view
    }

    private fun enroll(
        uemail: String?,
        pname: String,
        pdate: String,
        ppoint: String,
        plocation: String,
        pcycle: String,
        phour: Int,
        pminute: Int,
        ptemp: String,
        phumid: String,
        ptemp_alarm: Int,
        phumid_alarm: Int
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // PHP 스크립트의 URL
                val url = URL("http://10.0.2.2/enrollplant.php")

                // HttpURLConnection 열기
                val connection = url.openConnection() as HttpURLConnection

                // POST 요청 설정
                connection.requestMethod = "POST"
                connection.doOutput = true

                // POST 데이터 작성
                val postData = URLEncoder.encode("uemail", "UTF-8") + "=" + URLEncoder.encode(uemail, "UTF-8") + "&" +
                        URLEncoder.encode("pname", "UTF-8") + "=" + URLEncoder.encode(pname, "UTF-8") + "&" +
                        URLEncoder.encode("pdate", "UTF-8") + "=" + URLEncoder.encode(pdate, "UTF-8") + "&" +
                        URLEncoder.encode("ppoint", "UTF-8") + "=" + URLEncoder.encode(ppoint, "UTF-8") + "&" +
                        URLEncoder.encode("plocation", "UTF-8") + "=" + URLEncoder.encode(plocation, "UTF-8") + "&" +
                        URLEncoder.encode("pcycle", "UTF-8") + "=" + URLEncoder.encode(pcycle, "UTF-8") + "&" +
                        URLEncoder.encode("phour", "UTF-8") + "=" + URLEncoder.encode(phour.toString(), "UTF-8") + "&" +
                        URLEncoder.encode("pminute", "UTF-8") + "=" + URLEncoder.encode(pminute.toString(), "UTF-8") + "&" +
                        URLEncoder.encode("ptemp", "UTF-8") + "=" + URLEncoder.encode(ptemp, "UTF-8") + "&" +
                        URLEncoder.encode("phumid", "UTF-8") + "=" + URLEncoder.encode(phumid, "UTF-8") + "&" +
                        URLEncoder.encode("ptemp_alarm", "UTF-8") + "=" + URLEncoder.encode(ptemp_alarm.toString(), "UTF-8") + "&" +
                        URLEncoder.encode("phumid_alarm", "UTF-8") + "=" + URLEncoder.encode(phumid_alarm.toString(), "UTF-8")
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
                if (result == "registration successful") {
                    launch(Dispatchers.Main) {
                        //메인화면으로 넘어감
                        //val intent2 = Intent(requireContext(), HomeActivity::class.java)
                        //intent2.putExtra("userEmail", uemail)
                        //startActivity(intent2)
                        val transaction = requireActivity().supportFragmentManager.beginTransaction()

                        // PlantEnrollFragment 인스턴스를 생성합니다.
                        val fragment = Fragment_Home()
                        val bundle = Bundle()

                        // Bundle에 데이터를 담습니다.
                        bundle.putString("userEmail", userEmail)

                        // Fragment에 Bundle을 설정합니다.
                        fragment.arguments = bundle

                        // FragmentTransaction을 사용하여 PlantEnrollFragment로 전환합니다.
                        transaction.replace(R.id.container, fragment)
                        transaction.addToBackStack(null) // 이전 Fragment로 돌아갈 수 있도록 back stack에 추가합니다.
                        transaction.commit() // 변경 사항을 적용합니다.
                    }
                } else {
                    launch(Dispatchers.Main) {
                        AlertDialog.Builder(requireContext())
                            .setTitle("With P")
                            .setMessage("등록 실패")
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


}