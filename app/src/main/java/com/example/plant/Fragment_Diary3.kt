package com.example.plant

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.bumptech.glide.Glide
import com.github.clans.fab.FloatingActionMenu
import com.github.clans.fab.FloatingActionButton
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

class Fragment_Diary3: Fragment() {
    private lateinit var image: ImageView
    private lateinit var date: TextView
    private lateinit var name: TextView
    private lateinit var content: TextView
    private lateinit var title: TextView
    private lateinit var beforeBtn: ImageView
    private lateinit var floatingActionMenu: FloatingActionMenu
    private lateinit var editBtn: FloatingActionButton
    private lateinit var deleteBtn: FloatingActionButton
    private var dDate: String = ""
    private var plantId: Int = 0
    private var diaryId: Int = 0
    private var userEmail: String = ""
    private var plantName: String = ""
    private var enrollTime: String = ""
    private var diaryTitle: String = ""
    private var diaryContent: String = ""
    private var imageUrl: String = "http://192.168.233.22:80/uploads/default5.png"
    //private var imageUrl: String = "http://192.168.233.22:80/uploads/default5.png"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle the back press in the fragment
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val fragmentManager = requireActivity().supportFragmentManager
                fragmentManager.popBackStack(
                    null,
                    FragmentManager.POP_BACK_STACK_INCLUSIVE
                )
                val bundle = Bundle().apply {
                    putInt("itemId", diaryId)
                    putInt("plantId", plantId)
                    putString("plantName", plantName)
                    putString("userEmail", userEmail)
                    putString("diaryTitle", diaryTitle)
                    putString("diaryContent", diaryContent)
                    putString("imageUrl", imageUrl)
                    putString("diaryDate", dDate)
                    putString("enrollTime", enrollTime)
                }
                val fragmentDiary1 = Fragment_Diary1().apply {
                    arguments = bundle
                }
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                transaction.replace(R.id.container, fragmentDiary1)
                transaction.addToBackStack(null) // Add to back stack
                transaction.commit()
            }
        })
    }
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_diary3, container, false)
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.GONE
        arguments?.let {
            diaryId = it.getInt("itemId")
            plantId = it.getInt("plantId")
            userEmail = it.getString("userEmail").toString()
            plantName = it.getString("plantName").toString()
            enrollTime = it.getString("enrollTime").toString()
            imageUrl = it.getString("imageUrl").toString()
            diaryTitle = it.getString("diaryTitle").toString()
            diaryContent = it.getString("diaryContent").toString()
            dDate = it.getString("diaryDate").toString()
        }
        image = view.findViewById(R.id.detailImageView)
        date = view.findViewById(R.id.detailDate)
        title =view.findViewById(R.id.detailTitle)
        content =view.findViewById(R.id.detailContent)
        name = view.findViewById(R.id.detailName)
        beforeBtn = view.findViewById(R.id.beforeBtn2)
        floatingActionMenu = view.findViewById(R.id.floatingActionMenu)
        editBtn = view.findViewById(R.id.editBtn)
        deleteBtn = view.findViewById(R.id.deleteBtn)

        date.text = enrollTime
        title.text = diaryTitle
        content.text = diaryContent
        name.text = plantName

        Glide.with(requireContext())
            .load(imageUrl) // 이미지 URL
            .into(image) // 이미지뷰에 로드된 이미지 설정
        deleteBtn.setOnClickListener {
            //floatingActionMenu.close(true)
//            AlertDialog.Builder(requireContext())
//                .setTitle("With P")
//                .setMessage("정말 삭제하시겠습니까?")
//                .setPositiveButton("삭제") { dialog, _ ->
//                    deleteDiary(diaryId)
//                    dialog.dismiss() // 다이얼로그 닫기
//                }
//                .show()
            showdialog2(diaryId, "다이어리 삭제", "다이어리를 정말 삭제하시겠습니까?", "삭제", "취소")
        }
        beforeBtn.setOnClickListener {
            replaceFragment(Fragment_Diary1())
        }
        editBtn.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()

            val fragment = Fragment_Diary4()
            val bundle = Bundle()

            // Bundle에 데이터를 담기
            bundle.putInt("itemId", diaryId)
            bundle.putInt("plantId", plantId)
            bundle.putString("plantName", plantName)
            bundle.putString("userEmail", userEmail)
            bundle.putString("diaryTitle", diaryTitle)
            bundle.putString("diaryContent", diaryContent)
            bundle.putString("imageUrl", imageUrl)
            bundle.putString("diaryDate", dDate)
            bundle.putString("enrollTime", enrollTime)

            // Fragment에 Bundle을 설정
            fragment.arguments = bundle

            // FragmentTransaction을 사용하여 PlantEnrollFragment로 전환
            transaction.replace(R.id.container, fragment)
            transaction.addToBackStack(null) // 이전 Fragment로 돌아갈 수 있도록 back stack에 추가
            transaction.commit() // 변경 사항을 적용
        }
        return view
    }
    private fun deleteDiary(diaryid: Int) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // PHP 스크립트의 URL
                val url = URL("http://192.168.233.22:80/deletediary.php")

                // HttpURLConnection 열기
                val connection = url.openConnection() as HttpURLConnection

                // POST 요청 설정
                connection.requestMethod = "POST"
                connection.doOutput = true

                // POST 데이터 작성
                val postData = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(diaryid.toString(), "UTF-8")
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
                        replaceFragment(Fragment_Diary1())//Fragment_Diary1 으로 넘어감
                    }
                } else {
                    launch(Dispatchers.Main) {
//                        AlertDialog.Builder(requireContext())
//                            .setTitle("With P")
//                            .setMessage("삭제 실패")
//                            .setPositiveButton("확인") { dialog, which -> Log.d("MyTag", "positive") }
//                            .create()
//                            .show()
                        showdialog("","다이어리 삭제에 실패하였습니다.", "확인")
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


    private fun showdialog2(diaryid: Int, title: String, message: String, buttonText1: String, buttonText2: String) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.custom_dialog, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)

        val dialog = dialogBuilder.create()

        val dialogTitle = dialogView.findViewById<TextView>(R.id.dialogTitle)
        val dialogMessage = dialogView.findViewById<TextView>(R.id.dialogMessage)
        val positiveButton = dialogView.findViewById<Button>(R.id.positiveButton)
        val negativeButton = dialogView.findViewById<Button>(R.id.negativeButton)

        dialogTitle.text = title
        dialogMessage.text = message
        positiveButton.text = buttonText1
        negativeButton.text = buttonText2

        positiveButton.setOnClickListener {
            deleteDiary(diaryid)
            floatingActionMenu.close(true)
            dialog.dismiss()
        }

        negativeButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


    private fun replaceFragment(fragment: Fragment){
        /*
        val transaction = requireActivity().supportFragmentManager.beginTransaction()

        val fragment = fragment
        val bundle = Bundle()

        bundle.putInt("itemId", diaryId)
        bundle.putInt("plantId", plantId)
        bundle.putString("plantName", plantName)
        bundle.putString("userEmail", userEmail)
        bundle.putString("diaryTitle", diaryTitle)
        bundle.putString("diaryContent", diaryContent)
        bundle.putString("imageUrl", imageUrl)
        bundle.putString("diaryDate", dDate)
        bundle.putString("enrollTime", enrollTime)

        // Fragment에 Bundle을 설정
        fragment.arguments = bundle

        // FragmentTransaction을 사용하여 PlantEnrollFragment로 전환
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null) // 이전 Fragment로 돌아갈 수 있도록 back stack에 추가
        transaction.commit() // 변경 사항을 적용

         */
        val fragmentManager = requireActivity().supportFragmentManager
        fragmentManager.popBackStack(
            fragmentManager.getBackStackEntryAt(0).id,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )


        val bundle = Bundle().apply {
            putInt("itemId", diaryId)
            putInt("plantId", plantId)
            putString("plantName", plantName)
            putString("userEmail", userEmail)
            putString("diaryTitle", diaryTitle)
            putString("diaryContent", diaryContent)
            putString("imageUrl", imageUrl)
            putString("diaryDate", dDate)
            putString("enrollTime", enrollTime)
        }
        fragment.arguments = bundle

        val transaction = fragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }
}