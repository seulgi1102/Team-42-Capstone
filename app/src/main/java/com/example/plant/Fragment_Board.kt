package com.example.plant

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentManager

class Fragment_Board : Fragment() {
    private lateinit var FreeBoardFragment: Fragment_FreeBoard
    private lateinit var freeboard: Button
    private lateinit var selfboard: Button
    private lateinit var giveboard: Button
    private lateinit var itemboard: Button
    private lateinit var plantboard: Button
    private lateinit var qnaboard: Button
    private var userEmail: String? = null
    //private var board_type: Int? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment__board, container, false)
        arguments?.let {
            userEmail = it.getString("userEmail")
            //userPassword = it.getString("userPassword")
        }
//        val bundle = Bundle().apply {
//            putString("userEmail", userEmail)
//        }
//        FreeBoardFragment = Fragment_FreeBoard().apply {
//            arguments = bundle
//        }
        freeboard = view.findViewById(R.id.freeboard)
        selfboard = view.findViewById(R.id.selfboard)
        giveboard = view.findViewById(R.id.giveboard)
        itemboard = view.findViewById(R.id.itemboard)
        plantboard = view.findViewById(R.id.plantboard)
        qnaboard = view.findViewById(R.id.qnaboard)

        //val intent = Intent(requireContext(), FreeBoard_Fragment::class.java)
        freeboard.setOnClickListener {
            // 클릭 이벤트 처리
            //FreeBoardFragment = FreeBoard_Fragment() // 해당하는 프래그먼트로 변경
            //유저랑 게시판 번호 넘기기
            val bundle = Bundle().apply {
                putString("userEmail", userEmail)
                putInt("board_type",1)
            }
            FreeBoardFragment = Fragment_FreeBoard().apply {
                arguments = bundle
            }
            replaceFragment(FreeBoardFragment)
        }

        selfboard.setOnClickListener {
            // 클릭 이벤트 처리
            //FreeBoardFragment = FreeBoard_Fragment() // 해당하는 프래그먼트로 변경
            val bundle = Bundle().apply {
                putString("userEmail", userEmail)
                putInt("board_type",2)
            }
            FreeBoardFragment = Fragment_FreeBoard().apply {
                arguments = bundle
            }
            replaceFragment(FreeBoardFragment)
        }

        giveboard.setOnClickListener {
            // 클릭 이벤트 처리
            //FreeBoardFragment = FreeBoard_Fragment() // 해당하는 프래그먼트로 변경
            val bundle = Bundle().apply {
                putString("userEmail", userEmail)
                putInt("board_type",3)
            }
            FreeBoardFragment = Fragment_FreeBoard().apply {
                arguments = bundle
            }
            replaceFragment(FreeBoardFragment)
        }

        itemboard.setOnClickListener {
            // 클릭 이벤트 처리
            //FreeBoardFragment = FreeBoard_Fragment() // 해당하는 프래그먼트로 변경
            val bundle = Bundle().apply {
                putString("userEmail", userEmail)
                putInt("board_type",4)
            }
            FreeBoardFragment = Fragment_FreeBoard().apply {
                arguments = bundle
            }
            replaceFragment(FreeBoardFragment)
        }

        plantboard.setOnClickListener {
            // 클릭 이벤트 처리
            //FreeBoardFragment = FreeBoard_Fragment() // 해당하는 프래그먼트로 변경
            val bundle = Bundle().apply {
                putString("userEmail", userEmail)
                putInt("board_type",5)
            }
            FreeBoardFragment = Fragment_FreeBoard().apply {
                arguments = bundle
            }
            replaceFragment(FreeBoardFragment)
        }

        qnaboard.setOnClickListener {
            // 클릭 이벤트 처리
            //FreeBoardFragment = FreeBoard_Fragment() // 해당하는 프래그먼트로 변경
            val bundle = Bundle().apply {
                putString("userEmail", userEmail)
                putInt("board_type",6)
            }
            FreeBoardFragment = Fragment_FreeBoard().apply {
                arguments = bundle
            }
            replaceFragment(FreeBoardFragment)
        }
        return view
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = requireActivity().supportFragmentManager
        // Remove any existing instances of the same fragment
        fragmentManager.fragments.forEach {
            if (it::class == fragment::class) {
                fragmentManager.beginTransaction().remove(it).commitNow()
            }
        }
        // Replace the fragment
        fragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commitNow()
    }

}