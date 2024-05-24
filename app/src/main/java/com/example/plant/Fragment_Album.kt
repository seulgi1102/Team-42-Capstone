package com.example.plant

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.GridView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

class Fragment_Album : Fragment() {
    private var userEmail: String? = null
    private var pList: ArrayList<PlantListItem> = ArrayList()
    private lateinit var gridView: GridView
    private lateinit var option: Button
    private lateinit var albumGridAdapter: GridAlbumListAdapter
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_album, container, false)
        val bottomNavigationView = requireActivity().findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.visibility = View.VISIBLE
        gridView = view.findViewById(R.id.gridView)

        arguments?.let {
            userEmail = it.getString("userEmail")
        }
        GlobalScope.launch(Dispatchers.IO) {
            userEmail?.let { getPlantinfo(it) }
        }
        gridView.setOnItemClickListener { parent, view, position, id ->
            val item: PlantListItem = (parent.adapter as GridAlbumListAdapter).getItem(position) as PlantListItem
            //Toast.makeText(requireContext(), "Selected item ID: ${item.getItemId()}", Toast.LENGTH_SHORT).show()
            if(item.getPlantName()!="") {
                val transaction = requireActivity().supportFragmentManager.beginTransaction()
                val fragment = Fragment_Album2()
                val bundle = Bundle()
                var plantName = item.getPlantName()
                var plantId = item.getItemId()
                var plantDate = item.getPlantDate()
                var plantPoint = item.getPlantPoint()
                var plantHour = item.getPlantHour()
                var plantMinute = item.getPlantMinute()
                var plantPlace = item.getPlantLocation()
                var wateringCycle = item.getPlantCycle()
                var temperature = item.getPlantTemp()
                var humid = item.getPlantHumid()
                var wateringAlarm = item.getTempAlarm()
                var tempHumidAlarm = item.getHumidAlarm()
                var imageUrl = item.getImageUrl()
                var enrollTime = item.getEnrollTime()
                bundle.putString("userEmail", userEmail)
                bundle.putString("plantName", plantName)
                bundle.putInt("plantId", plantId)
                bundle.putString("plantDate", plantDate)
                bundle.putString("plantPoint", plantPoint)
                bundle.putInt("plantHour", plantHour)
                bundle.putInt("plantMinute", plantMinute)
                bundle.putString("plantPlace", plantPlace)
                bundle.putString("wateringCycle", wateringCycle)
                bundle.putString("imageUrl", imageUrl)
                bundle.putInt("wateringAlarm", wateringAlarm)
                bundle.putInt("tempHumidAlarm", tempHumidAlarm)
                bundle.putString("temperature", temperature)
                bundle.putString("enrollTime", enrollTime)
                bundle.putString("humid", humid)


                // Fragment에 Bundle을 설정
                fragment.arguments = bundle

                // FragmentTransaction을 사용하여 PlantEnrollFragment로 전환
                transaction.replace(R.id.container, fragment)
                transaction.addToBackStack(null) // 이전 Fragment로 돌아갈 수 있도록 back stack에 추가
                transaction.commit() // 변경 사항을 적용
            }
            //intent.putExtra("userEmail", userEmail)
            //startActivity(intent)
            // Toast.makeText(context, item.getNum().toString(), Toast.LENGTH_SHORT).show()
        }
        option = view.findViewById(R.id.options)
        option.setOnClickListener { view ->
            val popupMenu = PopupMenu(requireContext(), view)
            popupMenu.menuInflater.inflate(R.menu.popup_menu, popupMenu.menu)
            popupMenu.setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.menu1 -> {
                        pList.sortByDescending { SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(it.getEnrollTime()) }
                        requireActivity().runOnUiThread {
                            gridView.adapter = GridAlbumListAdapter(pList)
                        }
                        true
                    }
                    R.id.menu2 -> {
                        pList.sortBy { SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(it.getEnrollTime()) }
                        requireActivity().runOnUiThread {
                            gridView.adapter = GridAlbumListAdapter(pList)
                        }
                        true
                    }
                    else -> {
                        pList.sortByDescending { SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(it.getEnrollTime()) }
                        requireActivity().runOnUiThread {
                            gridView.adapter = GridAlbumListAdapter(pList)
                        }
                        true
                    }
                }
            }
            popupMenu.show()
        }
        albumGridAdapter =GridAlbumListAdapter(pList)
        gridView.adapter = albumGridAdapter
        return view
    }
    private fun getPlantinfo(uemail: String) {
        val url = URL("http://10.0.2.2/getplantinfo.php")
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "POST"
        connection.doOutput = true

        val postData = URLEncoder.encode("uemail", "UTF-8") + "=" + URLEncoder.encode(uemail, "UTF-8")
        val outputStream = OutputStreamWriter(connection.outputStream)
        outputStream.write(postData)
        outputStream.flush()
        outputStream.close()

        val inputStream = BufferedReader(InputStreamReader(connection.inputStream))
        val response = StringBuilder()
        var line: String?
        while (inputStream.readLine().also { line = it } != null) {
            response.append(line)
        }
        inputStream.close()

        val jsonResponse = JSONObject(response.toString())
        val status = jsonResponse.getString("status")
        if (status == "success") {
            val dataArray = jsonResponse.getJSONArray("data")
            // 가져온 데이터를 처리
            handleSuccess(dataArray)
        } else {
            // 실패 처리
            handleFailure()
        }
    }
    // 데이터 가져오기가 성공한 경우 처리할 로직
    private fun handleSuccess(dataArray: JSONArray) {
        //pList.clear()
        val newPList = ArrayList<PlantListItem>()
        for (i in 0 until dataArray.length()) {
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
            //pList.add(plantItem)
            // 리스트에 추가됨
            //pList.add(0, plantItem)
            // pList = plantList
        }
        newPList.sortByDescending { SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(it.getEnrollTime()) }
        //pList.sortByDescending { SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(it.getEnrollTime()) }
        requireActivity().runOnUiThread {
            //gridView.adapter = GridAlbumListAdapter(pList)
            albumGridAdapter.updateData(newPList)
        // GridView 업데이트 후 추가적인 작업 수행 가능
        }
        Log.d("MyTag", "Data retrieved successfully!")
    }

    private fun handleFailure() {
        // 실패한 경우
        pList.clear()
        val defaultItem = PlantListItem()
        defaultItem.setImageUrl("http://10.0.2.2/uploads/default4.png")
        requireActivity().runOnUiThread {
            //gridView.adapter = GridAlbumListAdapter(pList)
            albumGridAdapter.updateData(pList)
            // GridView 업데이트 후 추가적인 작업 수행 가능
        }
        //pList.add(defaultItem)
        //Toast.makeText(context, "Failed to retrieve data", Toast.LENGTH_SHORT).show()
    }
}