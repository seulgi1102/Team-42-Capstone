package com.example.plant

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView

class Fragment_Enroll1 : Fragment() {
    private lateinit var name: EditText
    private var userEmail: String? = null
    private lateinit var bundle: Bundle
    //var plantDataListener: DataListener? = null
    private lateinit var viewModel: PlantEnrollViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_enroll1, container, false)
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.GONE
        arguments?.let {
            userEmail = it.getString("userEmail")
        }
        bundle = Bundle()
        name = view.findViewById(R.id.plantName)

        //plantDataListener = (activity as? DataListener)
        viewModel = ViewModelProvider(requireActivity()).get(PlantEnrollViewModel::class.java)
        /*
        userEmail?.let {
            Toast.makeText(requireContext(), "User Email: $userEmail", Toast.LENGTH_SHORT).show()
        }*/
        return view
    }

    override fun onPause() {
        super.onPause()
        //plantDataListener?.onNameReceived(plantName)
        viewModel.pname = name.text.toString()
    }

}