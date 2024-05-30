package com.example.plant

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import me.relex.circleindicator.CircleIndicator3

class PlantEnrollFragment : Fragment() {

    private lateinit var viewpager: ViewPager2
    private lateinit var indicator: CircleIndicator3
    private lateinit var viewModel: PlantEnrollViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_plant_enroll, container, false)
        viewpager = view.findViewById(R.id.viewPager2)
        indicator = view.findViewById(R.id.home_pannel_indicator)

        viewModel = ViewModelProvider(this).get(PlantEnrollViewModel::class.java)
        val email = arguments?.getString("userEmail")

        val intent = Intent(requireContext(), HomeActivity::class.java)

        //Toast.makeText(requireContext(), "Email: $email", Toast.LENGTH_SHORT).show()
        val bundle = Bundle().apply {
            putString("userEmail", email)
        }

        val fragment1 = Fragment_Enroll1()
        val fragment2 = Fragment_Enroll2()
        val fragment3 = Fragment_Enroll3()
        val fragment4 = Fragment_Enroll4()

        var viewPagerEnrollAdapter = ViewPagerEnrollAdapter(requireActivity())
        viewPagerEnrollAdapter.addFragment(fragment1.apply {
            arguments = bundle // 프래그먼트에 데이터 전달
        })
        viewPagerEnrollAdapter.addFragment(fragment2)
        viewPagerEnrollAdapter.addFragment(fragment3)
        viewPagerEnrollAdapter.addFragment(fragment4.apply {
            arguments = bundle // 프래그먼트에 데이터 전달
        })

        viewpager.apply {
            adapter = viewPagerEnrollAdapter
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })
        }
        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                viewpager.currentItem = position
            }
        })
        indicator.setViewPager(viewpager)
        return view
    }
}