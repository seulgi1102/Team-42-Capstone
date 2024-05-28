package com.example.plant

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment

class Fragment_Step1 : Fragment() {
    private lateinit var FragmentHome: Fragment_Home
    private lateinit var backbtn: ImageView
    private var userEmail: String? = null
    private var imageUrl: String? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_step1, container, false)
        arguments?.let {
            userEmail = it.getString("userEmail")
            imageUrl = it.getString("imageUrl")
            //userPassword = it.getString("userPassword")
        }
        backbtn = view.findViewById(R.id.backBtn)
        backbtn.setOnClickListener {
            val bundle = Bundle().apply {
                putString("userEmail", userEmail)
                putString("imageUrl", imageUrl)
            }
            FragmentHome = Fragment_Home().apply {
                arguments = bundle
            }
            replaceFragment(FragmentHome)
        }
        return view
    }
    private fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment) // container는 프래그먼트가 표시될 영역의 ID
        transaction.addToBackStack(null) // 뒤로 가기 버튼을 눌렀을 때 이전 화면으로 돌아갈 수 있도록 스택에 추가
        transaction.commit()
    }
}