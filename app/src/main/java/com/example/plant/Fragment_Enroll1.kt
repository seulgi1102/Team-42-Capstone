package com.example.plant

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class Fragment_Enroll1 : Fragment() {
    @SuppressLint("MissingInflatedId")
    private lateinit var recyclerView: RecyclerView
    private lateinit var enrollBtn: Button
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_enroll1, container, false)
        val intent = Intent(requireContext(), PlantEnrollActivity::class.java)

        return view
    }
}