package com.example.plant

import ApiService
import ImageUploadResponse
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
class Fragment_Enroll4 : Fragment() {
    private var userEmail: String? = null
    private lateinit var enrollBtn: Button
    private lateinit var viewModel: PlantEnrollViewModel
    //private val ImageUrl = "http://10.0.2.2/uploads/default3.png"
    private var selectedImageUri: Uri? = null
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.233.22:80/")
        //.baseUrl("http://localhost:80/")
        .addConverterFactory(GsonConverterFactory.create()) // Gson 변환기 추가
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    private val apiService = retrofit.create(ApiService::class.java)

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
            selectedImageUri = viewModel.imageuri
            //val currentTimeMillis = System.currentTimeMillis()

            if (pnameText.isNotBlank()) {
                if (ptemp_alarm == 1) {
                    if (pcycleText.isNotBlank() && phourValue != 0 && pminuteValue != 0) {
                        if (selectedImageUri != null) {
                            val file = File(absolutelyPath(selectedImageUri, requireContext()))
                            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                            val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

                            // 이미지 업로드 후 콜백에서 식물 등록 요청
                            uploadImageAndGetUrl(body) { imageUrl ->
                                if (imageUrl != null) {
                                    Toast.makeText(requireContext(), "알람과 함께 등록", Toast.LENGTH_SHORT).show()
                                    GlobalScope.launch(Dispatchers.IO) {
                                        enroll(
                                            uemail, pnameText, pdateText, ppointText, plocationText, pcycleText,
                                            phourValue, pminuteValue, ptempText, phumidText, ptemp_alarm, phumid_alarm, imageUrl
                                        )
                                    }
                                } else {
                                    Toast.makeText(requireContext(), "이미지 업로드 실패", Toast.LENGTH_SHORT).show()
                                }
                            }
                        } else {
                            Toast.makeText(requireContext(), "이미지 없이 등록", Toast.LENGTH_SHORT).show()
                            GlobalScope.launch(Dispatchers.IO) {
                                enroll(
                                    uemail, pnameText, pdateText, ppointText, plocationText, pcycleText,
                                    phourValue, pminuteValue, ptempText, phumidText, ptemp_alarm, phumid_alarm, ImageUrl
                                )
                            }
                        }
                    } else {
                        showdialog("식물 등록 실패", "시간과 주기를 설정하세요.", "확인")
                    }
                } else {
                    if (selectedImageUri != null) {
                        val file = File(absolutelyPath(selectedImageUri, requireContext()))
                        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

                        // 이미지 업로드 후 콜백에서 식물 등록 요청
                        uploadImageAndGetUrl(body) { imageUrl ->
                            if (imageUrl != null) {
                                Toast.makeText(requireContext(), "알람 없이 등록", Toast.LENGTH_SHORT).show()
                                GlobalScope.launch(Dispatchers.IO) {
                                    enroll(
                                        uemail, pnameText, pdateText, ppointText, plocationText, pcycleText,
                                        phourValue, pminuteValue, ptempText, phumidText, ptemp_alarm, phumid_alarm, imageUrl
                                    )
                                }
                            } else {
                                Toast.makeText(requireContext(), "이미지 업로드 실패", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        Toast.makeText(requireContext(), "이미지 없이 등록", Toast.LENGTH_SHORT).show()
                        GlobalScope.launch(Dispatchers.IO) {
                            enroll(
                                uemail, pnameText, pdateText, ppointText, plocationText, pcycleText,
                                phourValue, pminuteValue, ptempText, phumidText, ptemp_alarm, phumid_alarm, ImageUrl
                            )
                        }
                    }
                }
            } else {
                showdialog("식물 등록 실패", "식물의 이름을 입력해주세요.", "확인")
            }
        }
        return view
    }

    private fun uploadImageAndGetUrl(body: MultipartBody.Part, callback: (String?) -> Unit) {
        apiService.sendImage(body).enqueue(object : Callback<ImageUploadResponse> {
            override fun onResponse(call: Call<ImageUploadResponse>, response: Response<ImageUploadResponse>) {
                if (response.isSuccessful) {
                    val imageUploadResponse = response.body()
                    val imageUrl = imageUploadResponse?.imageUrl
                    imageUrl?.let {
                        Log.d("이미지 업로드 성공", imageUrl)
                        callback(imageUrl) // 이미지 URL을 콜백으로 전달
                    } ?: run {
                        // 이미지 URL이 없는 경우, 실패 콜백 호출
                        callback(null)
                    }
                } else {
                    // 서버 응답이 실패한 경우, 실패 콜백 호출
                    callback(null)
                }
            }

            override fun onFailure(call: Call<ImageUploadResponse>, t: Throwable) {
                // 네트워크 오류 또는 예외 발생 시 실패 콜백 호출
                Log.e("sendImage", "이미지 업로드 실패", t)
                callback(null)
            }
        })
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
        phumid_alarm: Int,
        imageurl: String
    ) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // PHP 스크립트의 URL
                val url = URL("http://192.168.233.22:80/enrollplant.php")

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
                        URLEncoder.encode("phumid_alarm", "UTF-8") + "=" + URLEncoder.encode(phumid_alarm.toString(), "UTF-8") + "&" +
                        URLEncoder.encode("imageurl", "UTF-8") + "=" + URLEncoder.encode(imageurl, "UTF-8")

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
                        val fragmentManager = requireActivity().supportFragmentManager
                        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
                        val bundle = Bundle().apply {
                            putString("userEmail", userEmail)
                        }
                        val fragment = Fragment_Home()
                        fragment.arguments = bundle

                        // 프래그먼트 교체
                        val transaction = fragmentManager.beginTransaction()
                        transaction.replace(R.id.container, fragment)
                        transaction.commit()
                    }
                } else {
                    launch(Dispatchers.Main) {
//                        AlertDialog.Builder(requireContext())
//                            .setTitle("With P")
//                            .setMessage("등록 실패")
//                            .setPositiveButton("확인") { dialog, which -> Log.d("MyTag", "positive") }
//                            .create()
//                            .show()
                        showdialog("", "식물 등록에 실패하였습니다.", "확인")
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

    private fun showdialog(title: String, message: String, buttonText: String) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialog2, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)

        val dialog = dialogBuilder.create()

        val dialogTitle = dialogView.findViewById<TextView>(R.id.dialogTitle)
        val dialogMessage = dialogView.findViewById<TextView>(R.id.dialogMessage)
        val positiveButton = dialogView.findViewById<Button>(R.id.dialogButton)

        dialogTitle.text = title
        dialogMessage.text = message
        positiveButton.text = buttonText

        positiveButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    //uri로 이미지의 절대경로 얻어오기
    private fun absolutelyPath(path: Uri?, context: Context): String{
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()
        var result = c?.getString(index!!)

        return result!!
    }
}