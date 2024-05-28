package com.example.plant

import PlantItem
import RecyclerViewPlantAdapter
import RecyclerViewStepAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import java.text.SimpleDateFormat
import java.util.Locale

class Fragment_Home : Fragment() {

    private lateinit var FragmentRecommendPlant1: Fragment_Recommend_Plant1
    private lateinit var FragmentRecommendPlant2: Fragment_Recommend_Plant2
    private lateinit var FragmentRecommendPlant3: Fragment_Recommend_Plant3
    private lateinit var FragmentRecommendPlant4: Fragment_Recommend_Plant4
    private lateinit var FragmentRecommendPlant5: Fragment_Recommend_Plant5
    private lateinit var FragmentRecommendPlant6: Fragment_Recommend_Plant6
    private lateinit var step1:LinearLayout
    private lateinit var step2:LinearLayout
    private lateinit var step3:LinearLayout
    private lateinit var step4:LinearLayout
    private lateinit var step5:LinearLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var enrollBtn: Button
    private var userEmail: String? = null
    private var imageUrl: String? = null
    private var pList: ArrayList<PlantListItem> = ArrayList()
    private lateinit var plantAdapter: RecyclerViewPlantAdapter

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment__home, container, false)
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.VISIBLE
        arguments?.let {
            userEmail = it.getString("userEmail")
            imageUrl = it.getString("imageUrl")
        }

        viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
            userEmail?.let { getPlantInfo(it) }
        }

        recyclerView = view.findViewById(R.id.recyclerView)
        enrollBtn = view.findViewById(R.id.button)
        enrollBtn.setOnClickListener {
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            val enrollFragment1 = PlantEnrollFragment()
            val bundle = Bundle().apply {
                putString("userEmail", userEmail)
            }
            enrollFragment1.arguments = bundle
            transaction.replace(R.id.container, enrollFragment1)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        plantAdapter = RecyclerViewPlantAdapter(pList)
        recyclerView.adapter = plantAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recyclerView.addItemDecoration(RecyclerViewDecoration(30))

        //추천 식물1 상세보기
        val recommendplant1 : LinearLayout = view.findViewById(R.id.recommendedPlant1)
        recommendplant1.setOnClickListener {
            //replaceFragment(Fragment_Recommend_Plant1())
            val bundle = Bundle().apply {
                putString("userEmail", userEmail)
                putString("imageUrl",imageUrl)
            }
            FragmentRecommendPlant1 = Fragment_Recommend_Plant1().apply {
                arguments = bundle
            }
            replaceFragment(FragmentRecommendPlant1)
        }

        //추천 식물2 상세보기
        val recommendplant2 : LinearLayout = view.findViewById(R.id.recommendedPlant2)
        recommendplant2.setOnClickListener {
            //replaceFragment(Fragment_Recommend_Plant1())
            val bundle = Bundle().apply {
                putString("userEmail", userEmail)
                putString("imageUrl",imageUrl)
            }
            FragmentRecommendPlant2 = Fragment_Recommend_Plant2().apply {
                arguments = bundle
            }
            replaceFragment(FragmentRecommendPlant2)
        }

        //추천 식물3 상세보기
        val recommendplant3 : LinearLayout = view.findViewById(R.id.recommendedPlant3)
        recommendplant3.setOnClickListener {
            //replaceFragment(Fragment_Recommend_Plant1())
            val bundle = Bundle().apply {
                putString("userEmail", userEmail)
                putString("imageUrl",imageUrl)
            }
            FragmentRecommendPlant3 = Fragment_Recommend_Plant3().apply {
                arguments = bundle
            }
            replaceFragment(FragmentRecommendPlant3)
        }

        //추천 식물4 상세보기
        val recommendplant4 : LinearLayout = view.findViewById(R.id.recommendedPlant4)
        recommendplant4.setOnClickListener {
            //replaceFragment(Fragment_Recommend_Plant1())
            val bundle = Bundle().apply {
                putString("userEmail", userEmail)
                putString("imageUrl",imageUrl)
            }
            FragmentRecommendPlant4 = Fragment_Recommend_Plant4().apply {
                arguments = bundle
            }
            replaceFragment(FragmentRecommendPlant4)
        }

        //추천 식물5 상세보기
        val recommendplant5 : LinearLayout = view.findViewById(R.id.recommendedPlant5)
        recommendplant5.setOnClickListener {
            //replaceFragment(Fragment_Recommend_Plant1())
            val bundle = Bundle().apply {
                putString("userEmail", userEmail)
                putString("imageUrl",imageUrl)
            }
            FragmentRecommendPlant5 = Fragment_Recommend_Plant5().apply {
                arguments = bundle
            }
            replaceFragment(FragmentRecommendPlant5)
        }

        //추천 식물6 상세보기
        val recommendplant6 : LinearLayout = view.findViewById(R.id.recommendedPlant6)
        recommendplant6.setOnClickListener {
            //replaceFragment(Fragment_Recommend_Plant1())
            val bundle = Bundle().apply {
                putString("userEmail", userEmail)
                putString("imageUrl",imageUrl)
            }
            FragmentRecommendPlant6 = Fragment_Recommend_Plant6().apply {
                arguments = bundle
            }
            replaceFragment(FragmentRecommendPlant6)
        }

        step1 = view.findViewById(R.id.step1)
        step2 = view.findViewById(R.id.step2)
        step3 = view.findViewById(R.id.step3)
        step4 = view.findViewById(R.id.step4)
        step5 = view.findViewById(R.id.step5)

        step1.setOnClickListener{
            replaceFragment(Fragment_Step1())
        }
        step2.setOnClickListener{
            replaceFragment(Fragment_Step2())
        }
        step3.setOnClickListener{
            replaceFragment(Fragment_Step3())
        }
        step4.setOnClickListener{
            replaceFragment(Fragment_Step4())
        }
        step5.setOnClickListener{
            replaceFragment(Fragment_Step5())
        }
        return view
    }
    private suspend fun getPlantInfo(uemail: String) {
        withContext(Dispatchers.IO) {
            val url = URL("http://192.168.233.22:80/getplantinfo.php")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.doOutput = true

            val postData = URLEncoder.encode("uemail", "UTF-8") + "=" + URLEncoder.encode(uemail, "UTF-8")
            OutputStreamWriter(connection.outputStream).use {
                it.write(postData)
                it.flush()
            }

            val response = StringBuilder()
            BufferedReader(InputStreamReader(connection.inputStream)).use {
                var line: String?
                while (it.readLine().also { line = it } != null) {
                    response.append(line)
                }
            }

            val jsonResponse = JSONObject(response.toString())
            val status = jsonResponse.getString("status")
            if (status == "success") {
                val dataArray = jsonResponse.getJSONArray("data")
                handleSuccess(dataArray)
            } else {
                handleFailure()
            }
        }
    }

    private suspend fun handleSuccess(dataArray: JSONArray) {
        val newPList = ArrayList<PlantListItem>()
        val startIndex = if (dataArray.length() <= 3) 0 else dataArray.length() - 3
        for (i in (startIndex until dataArray.length()).reversed()) {
            val dataObject = dataArray.getJSONObject(i)
            val plantItem = PlantListItem().apply {
                setItemId(dataObject.getInt("id"))
                setUserEmail(dataObject.getString("uemail"))
                setPlantName(dataObject.getString("pname"))
                setPlantDate(dataObject.getString("pdate"))
                setPlantPoint(dataObject.getString("ppoint"))
                setPlantLocation(dataObject.getString("plocation"))
                setPlantCycle(dataObject.getString("pcycle"))
                setPlantHour(dataObject.getInt("phour"))
                setPlantMinute(dataObject.getInt("pminute"))
                setPlantTemp(dataObject.getString("ptemp"))
                setPlantHumid(dataObject.getString("phumid"))
                setTempAlarm(dataObject.getInt("ptemp_alarm"))
                setHumidAlarm(dataObject.getInt("phumid_alarm"))
                setImageUrl(dataObject.getString("imageurl"))
                setEnrollTime(dataObject.getString("currenttime"))
            }
            newPList.add(plantItem)
        }
        newPList.sortByDescending { SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(it.getEnrollTime()) }

        withContext(Dispatchers.Main) {
            plantAdapter.updateData(newPList)
            Log.d("MyTag", "Data retrieved successfully!")
        }
    }

    private suspend fun handleFailure() {
        withContext(Dispatchers.Main) {
            pList.clear()
            val defaultItem = PlantListItem().apply {
                setImageUrl("http://192.168.233.22:80/uploads/default4.png")
                setPlantName("hh")
            }
            pList.add(defaultItem)
            plantAdapter.updateData(pList)
            Log.d("MyTag", "Default item added to the list")
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }
}