package com.example.plant

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
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

const val GALLERY_REQUEST_CODE = 1001

class Fragment_Diary2 : Fragment() {
    private lateinit var image: ImageView
    private lateinit var date: TextView
    private lateinit var beforeBtn: Button
    private lateinit var saveBtn: Button
    private var userEmail: String = ""
    private var plantName: String = ""
    private var plantId: Int = 0
    private var dDate: String = ""
    private lateinit var diaryTitle: EditText
    private lateinit var diaryContent: EditText
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diary2, container, false)

        //val choiceDate = requireActivity().intent.getStringExtra("date")
        image = view.findViewById(R.id.imageView)
        date = view.findViewById(R.id.date2)
        beforeBtn = view.findViewById(R.id.beforBtn)
        saveBtn = view.findViewById(R.id.saveBtn)
        diaryTitle = view.findViewById(R.id.enterTitle)
        diaryContent = view.findViewById(R.id.enterContent)

        arguments?.let {
            userEmail = it.getString("userEmail").toString()
            plantName = it.getString("plantName").toString()
            plantId = it.getInt("plantId")
            dDate = it.getString("diaryDate").toString()
        }
        date.text = dDate
        beforeBtn.setOnClickListener {
           // val fragmentManager = requireActivity().supportFragmentManager
           // fragmentManager.popBackStack()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()

            // PlantEnrollFragment 인스턴스를 생성
            val fragment = Fragment_Diary1()
            val bundle = Bundle()

            // Bundle에 데이터를 담기
            bundle.putString("userEmail", userEmail)
            bundle.putString("plantName", plantName)
            bundle.putInt("plantId", plantId)
            bundle.putString("diaryDate", dDate)

            // Fragment에 Bundle을 설정
            fragment.arguments = bundle

            // FragmentTransaction을 사용하여 PlantEnrollFragment로 전환
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // 이전 Fragment로 돌아갈 수 있도록 back stack에 추가
            transaction.commit() // 변경 사항을 적용
        }
        image.setOnClickListener {
            // 갤러리로 접근하기 위한 Intent 생성
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*" // 이미지 타입으로 필터링

            // 갤러리 앱을 실행하고 이미지를 선택할 수 있도록 요청
            startActivityForResult(intent, GALLERY_REQUEST_CODE)
        }
        saveBtn.setOnClickListener {
            val title = diaryTitle.text.toString()
            val content = diaryContent.text.toString()
            GlobalScope.launch(Dispatchers.IO) {
                plantEnroll(plantId,plantName,userEmail, dDate, title, content)
            }
        }

        return view
    }

    private fun plantEnroll(plantid: Int, pname: String, uemail: String, ddate: String, dtitle: String, dcontent: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // PHP 스크립트의 URL
                val url = URL("http://10.0.2.2/enrolldiary.php")

                // HttpURLConnection 열기
                val connection = url.openConnection() as HttpURLConnection

                // POST 요청 설정
                connection.requestMethod = "POST"
                connection.doOutput = true

                // POST 데이터 작성
                val postData = URLEncoder.encode("plantid", "UTF-8") + "=" + URLEncoder.encode(plantid.toString(), "UTF-8") + "&" +
                        URLEncoder.encode("pname", "UTF-8") + "=" + URLEncoder.encode(pname, "UTF-8") + "&" +
                        URLEncoder.encode("uemail", "UTF-8") + "=" + URLEncoder.encode(uemail, "UTF-8") + "&" +
                        URLEncoder.encode("ddate", "UTF-8") + "=" + URLEncoder.encode(ddate, "UTF-8") + "&" +
                        URLEncoder.encode("dtitle", "UTF-8") + "=" + URLEncoder.encode(dtitle, "UTF-8") + "&" +
                        URLEncoder.encode("dcontent", "UTF-8") + "=" + URLEncoder.encode(dcontent, "UTF-8")
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
                        //Fragment_Diary1 으로 넘어감
                        val transaction = requireActivity().supportFragmentManager.beginTransaction()

                        // PlantEnrollFragment 인스턴스를 생성
                        val fragment = Fragment_Diary1()
                        val bundle = Bundle()

                        // Bundle에 데이터를 담기
                        bundle.putString("userEmail", userEmail)
                        bundle.putString("plantName", plantName)
                        bundle.putInt("plantId", plantId)
                        bundle.putString("diaryDate", dDate)

                        // Fragment에 Bundle을 설정
                        fragment.arguments = bundle

                        // FragmentTransaction을 사용하여 PlantEnrollFragment로 전환
                        transaction.replace(R.id.container, fragment)
                        transaction.addToBackStack(null) // 이전 Fragment로 돌아갈 수 있도록 back stack에 추가
                        transaction.commit() // 변경 사항을 적용
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