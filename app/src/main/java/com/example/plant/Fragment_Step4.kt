package com.example.plant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.FragmentManager

class Fragment_Step4 : Fragment() {
    private lateinit var fragmentHome: Fragment_Home
    private var userEmail: String? = null
    private var imageUrl: String? = null
    private lateinit var backbtn: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Handle the back press in the fragment
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                navigateToHomeFragment()
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_step4, container, false)

        arguments?.let {
            userEmail = it.getString("userEmail")
            imageUrl = it.getString("imageUrl")
        }

        backbtn = view.findViewById(R.id.backBtn)
        backbtn.setOnClickListener {
            navigateToHomeFragment()
        }

        return view
    }

    private fun navigateToHomeFragment() {
        val bundle = Bundle().apply {
            putString("userEmail", userEmail)
            putString("imageUrl", imageUrl)
        }
        fragmentHome = Fragment_Home().apply {
            arguments = bundle
        }
        replaceFragment(fragmentHome)
    }

    private fun replaceFragment(fragment: Fragment) {
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, fragment)
        //transaction.addToBackStack(null) // Add to back stack
        transaction.commit()
    }
}