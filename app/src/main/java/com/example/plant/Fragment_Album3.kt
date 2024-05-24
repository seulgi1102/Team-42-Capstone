package com.example.plant

import ApiService
import ImageUploadResponse
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.NumberPicker
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
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
import java.text.SimpleDateFormat
import java.util.Calendar

class Fragment_Album3 : Fragment(), View.OnClickListener{
    private val GALLERY_REQUEST_CODE = 12
    private val REQ_CAMERA = 11
    private var selectedDays = ArrayList<String>()
    private var userEmail: String = ""
    private var plantName: String = ""
    private var plantId: Int = 0
    private var plantDate: String = ""
    private var plantPoint: String = ""
    private var plantHour: Int = 0
    private var plantMinute: Int = 0
    private var plantPlace: String = ""
    private var wateringCycle: String =  ""
    private var ImageUrl: String = ""
    private var wateringAlarm: Int = 0
    private var tempHumidAlarm: Int = 0
    private var temperature: String = ""
    private var humid: String = ""
    private var enrollTime: String = ""
    private lateinit var date: TextView
    private lateinit var place: EditText
    private lateinit var pname: EditText
    private lateinit var point: EditText
    private lateinit var phumid: TextView
    private lateinit var ptemp: TextView
    private lateinit var ptime: TextView
    private lateinit var image: ImageView
    private lateinit var sunday: TextView
    private lateinit var monday: TextView
    private lateinit var tuesday: TextView
    private lateinit var wednesday: TextView
    private lateinit var thursday: TextView
    private lateinit var friday: TextView
    private lateinit var saturday: TextView
    private lateinit var wAlarm: Switch
    private lateinit var thAlarm: Switch
    private lateinit var calendarBtn: Button
    private lateinit var timeBtn: Button
    private lateinit var tempBtn: Button
    private lateinit var humidBtn: Button
    private lateinit var beforeBtn: ImageView
    private lateinit var editBtn: FloatingActionButton
    private lateinit var gallery: ImageView
    private lateinit var camera: ImageView
    //private var imageUrl: String = "http://10.0.2.2/uploads/default5.png"
    private var selectedImageUri: Uri? = null
    private val retrofit = Retrofit.Builder()
        .baseUrl("http://10.0.2.2/")
        .addConverterFactory(GsonConverterFactory.create()) // Gson 변환기 추가
        .addConverterFactory(ScalarsConverterFactory.create())
        .build()
    private val apiService = retrofit.create(ApiService::class.java)
    //권한 요청 처리
    private val galleryPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // 권한이 허용되면 갤러리 열기
            openGallery()
        } else {
            // 권한 거부되면 거절됐다는 메시지 보여주기
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            // 권한이 허용되면 갤러리 열기
            openCamera()
        } else {
            // 권한 거부되면 거절됐다는 메시지 보여주기
            Toast.makeText(requireContext(), "Permission denied", Toast.LENGTH_SHORT).show()
        }
    }
    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_album3, container, false)
        arguments?.let {
            userEmail = it.getString("userEmail").toString()
            plantName = it.getString("plantName").toString()
            plantId = it.getInt("plantId")
            plantDate = it.getString("plantDate").toString()
            plantPoint = it.getString("plantPoint").toString()
            plantHour = it.getInt("plantHour")
            plantMinute = it.getInt("plantMinute")
            plantPlace = it.getString("plantPlace").toString()
            wateringCycle = it.getString("wateringCycle").toString()
            ImageUrl = it.getString("imageUrl").toString()
            wateringAlarm = it.getInt("wateringAlarm")
            tempHumidAlarm = it.getInt("tempHumidAlarm")
            temperature = it.getString("temperature").toString()
            enrollTime = it.getString("enrollTime").toString()
            humid = it.getString("humid").toString()
        }
        date = view.findViewById(R.id.date)
        pname = view.findViewById(R.id.detailName)
        place = view.findViewById(R.id.place)
        phumid = view.findViewById(R.id.humid)
        ptemp = view.findViewById(R.id.temp)
        point = view.findViewById(R.id.plantDtl)
        ptime = view.findViewById(R.id.wateringTime)
        image = view.findViewById(R.id.detailImageView)
        beforeBtn = view.findViewById(R.id.beforeBtn)
        editBtn = view.findViewById(R.id.editBtn)
        wAlarm = view.findViewById(R.id.wateringSwitch)
        thAlarm = view.findViewById(R.id.tempHumidSwitch)
        calendarBtn = view.findViewById(R.id.calenderBtn)
        timeBtn = view.findViewById(R.id.timeBtn)
        tempBtn = view.findViewById(R.id.tempBtn)
        humidBtn = view.findViewById(R.id.humidBtn)
        sunday = view.findViewById(R.id.sun)
        monday = view.findViewById(R.id.mon)
        tuesday = view.findViewById(R.id.tue)
        wednesday = view.findViewById(R.id.wed)
        thursday = view.findViewById(R.id.thur)
        friday = view.findViewById(R.id.fri)
        saturday = view.findViewById(R.id.sat)
        gallery = view.findViewById(R.id.gallery)
        camera = view.findViewById(R.id.camera)

        gallery.setOnClickListener {
            //이미지 권한확인, sdk 33이상이면 READ_MEDIA_IMAGES, 이하면 READ_EXTERNAL_STORAGE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                galleryPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
            else
                galleryPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        camera.setOnClickListener {
            requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }

        sunday.setOnClickListener(this)
        monday.setOnClickListener(this)
        tuesday.setOnClickListener(this)
        wednesday.setOnClickListener(this)
        thursday.setOnClickListener(this)
        friday.setOnClickListener(this)
        saturday.setOnClickListener(this)
        selectedDays = wateringCycle?.split(", ")?.let { ArrayList(it) } ?: ArrayList<String>()
        for(day in selectedDays){
            if(day == "일"){
                val gradientDrawable = ResourcesCompat.getDrawable(resources, R.drawable.button_diary, null)
                sunday.background = gradientDrawable
            }else if(day == "월"){
                val gradientDrawable = ResourcesCompat.getDrawable(resources, R.drawable.button_diary, null)
                monday.background = gradientDrawable
            }else if(day == "화"){
                val gradientDrawable = ResourcesCompat.getDrawable(resources, R.drawable.button_diary, null)
                tuesday.background = gradientDrawable
            }else if(day == "수"){
                val gradientDrawable = ResourcesCompat.getDrawable(resources, R.drawable.button_diary, null)
                wednesday.background = gradientDrawable
            }else if(day == "목"){
                val gradientDrawable = ResourcesCompat.getDrawable(resources, R.drawable.button_diary, null)
                thursday.background = gradientDrawable
            }else if(day == "금"){
                val gradientDrawable = ResourcesCompat.getDrawable(resources, R.drawable.button_diary, null)
                friday.background = gradientDrawable
            }else if(day == "토"){
                val gradientDrawable = ResourcesCompat.getDrawable(resources, R.drawable.button_diary, null)
                saturday.background = gradientDrawable
            }else{}
        }


        calendarBtn.setOnClickListener {
            showDatePickerDialog()
        }
        tempBtn.setOnClickListener {
            val temperaturePicker = NumberPicker(requireContext())
            temperaturePicker.minValue = 10
            temperaturePicker.maxValue = 50
            temperaturePicker.value = 10

            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Select Temperature")
                .setView(temperaturePicker)
                .setPositiveButton("OK") { dialog, which ->
                    // 온도를 선택한 후 실행할 코드를 작성
                    temperature = temperaturePicker.value.toString() + "°C" + " ~ " + (temperaturePicker.value +15).toString() + "°C"
                    ptemp.text = temperature
                // 선택된 온도에 대한 작업 수행
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
        }
        humidBtn.setOnClickListener {
            val humidPicker = NumberPicker(requireContext())
            humidPicker.minValue = 30
            humidPicker.maxValue = 80
            humidPicker.value = 40

            val dialog = AlertDialog.Builder(requireContext())
                .setTitle("Select Humidity")
                .setView(humidPicker)
                .setPositiveButton("OK") { dialog, which ->
                    // 습도를 선택한 후 실행할 코드를 작성
                    humid = humidPicker.value.toString() + "%" + " ~ " + (humidPicker.value+20) + "%"
                    phumid.text = humid
                    // 선택된 습도에 대한 작업 수행
                }
                .setNegativeButton("Cancel", null)
                .create()

            dialog.show()
        }
        timeBtn.setOnClickListener {
            val calendar = Calendar.getInstance()
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)

            val timePickerDialog = TimePickerDialog(requireContext(), { view, hourOfDay, minute ->
                // 선택된 시간을 처리하는 코드를 여기에 작성
                plantHour = hourOfDay
                plantMinute = minute
                ptime.text = plantHour.toString() + ":" + plantMinute.toString()
                // 선택된 시간을 사용하거나 표시하는 등의 작업 수행
            }, hour, minute, true) // true를 전달하여 24시간 형식을 사용하도록 설정

            timePickerDialog.show()
        }
        wAlarm.setOnCheckedChangeListener { _, isChecked ->
            wateringAlarm = if (isChecked) 1 else 0
        }

        // humidSwitch의 상태가 변경될 때 해당 변수를 업데이트합니다.
        thAlarm.setOnCheckedChangeListener { _, isChecked ->
            tempHumidAlarm = if (isChecked) 1 else 0
        }
        beforeBtn.setOnClickListener {
            replaceFragment(Fragment_Album2())
        }
        editBtn.setOnClickListener {
            val pname = pname.text.toString()
            val point = point.text.toString()
            val place = place.text.toString()
            plantName = pname
            plantPoint = point
            plantPlace = place
            val selectedStringDays = selectedDays.joinToString(", ")
            wateringCycle = selectedStringDays
                if(pname.isNotEmpty()){
                    //선택된 이미지가 있으면
                    if (selectedImageUri != null) {
                        val file = File(absolutelyPath(selectedImageUri, requireContext())) // Assuming selectedImageUri is a file URI
                        val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
                        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)
                        Log.d("test", file.name) // 파일이름 출력
                        uploadImageAndGetUrl(body) { imageUrl ->
                            if (imageUrl != null) {
                                ImageUrl = imageUrl
                                GlobalScope.launch(Dispatchers.IO) {
                                    editPlant(plantId, enrollTime, pname, plantDate, point, place, selectedStringDays,
                                        plantHour, plantMinute, temperature, humid, wateringAlarm, tempHumidAlarm, imageUrl)
                                }
                            } else {
                                // 이미지 업로드 실패 처리
                                Toast.makeText(requireContext(), "이미지 uri가있으나 서버로부터 url을 받지못함", Toast.LENGTH_SHORT).show()
                            }
                        }
                    } else {
                        // No image selected, proceed without uploading image
                        GlobalScope.launch(Dispatchers.IO) {
                            editPlant(plantId, enrollTime, pname, plantDate, plantPoint, plantPlace, selectedStringDays,
                                plantHour, plantMinute, temperature, humid, wateringAlarm, tempHumidAlarm, ImageUrl)
                        }
                    }
            }else{
                AlertDialog.Builder(requireContext())
                    .setTitle("With P")
                    .setMessage("식물의 이름을 입력해주세요.")
                    .setPositiveButton("확인") { dialog, _ ->
                        dialog.dismiss() // 다이얼로그 닫기
                    }
                    .show()
            }
        }
        Glide.with(requireContext())
            .load(ImageUrl) // 이미지 URL
            .into(image) // 이미지뷰에 로드된 이미지 설정
        pname.setText(plantName)
        place.setText(plantPlace)
        point.setText(plantPoint)
        wAlarm.isChecked = wateringAlarm == 1
        thAlarm.isChecked = tempHumidAlarm == 1
        ptime.text = plantHour.toString() + ":" + plantMinute.toString()
        date.text = plantDate
        phumid.text = humid
        ptemp.text = temperature
        return view
    }
    fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), { view, selectedYear, selectedMonth, selectedDay ->
            // 선택된 날짜를 처리하는 코드를 여기에 작성
            plantDate = String.format("%d / %02d / %02d", selectedYear, selectedMonth + 1, selectedDay)
            date.text = plantDate
            // 선택된 날짜를 사용하거나 표시하는 등의 작업 수행
        }, year, month, day)

        datePickerDialog.show()
    }
    override fun onClick(v: View?) {
        // Check which TextView was clicked
        when (v?.id) {
            R.id.sun -> handleDayClick(sunday, "일")
            R.id.mon -> handleDayClick(monday, "월")
            R.id.tue -> handleDayClick(tuesday, "화")
            R.id.wed -> handleDayClick(wednesday, "수")
            R.id.thur -> handleDayClick(thursday, "목")
            R.id.fri -> handleDayClick(friday, "금")
            R.id.sat -> handleDayClick(saturday, "토")
        }
    }

    private fun handleDayClick(textView: TextView, day: String) {
        if (selectedDays.contains(day)) {
            textView.setBackgroundColor(Color.TRANSPARENT)
            selectedDays.remove(day)
        } else {
            val gradientDrawable = ResourcesCompat.getDrawable(resources, R.drawable.button_diary, null)
            textView.background = gradientDrawable
            //textView.setBackgroundColor(R.drawable.button_diary)
            selectedDays.add(day)
        }
        Log.d("Selected Days", selectedDays.toString())
    }
    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, GALLERY_REQUEST_CODE) //onActivityResult()
    }
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        createImageUri(newFileName(), "image/jpg")?.let {
            selectedImageUri = it
            intent.putExtra(MediaStore.EXTRA_OUTPUT, selectedImageUri)
            startActivityForResult(intent, REQ_CAMERA)
        }
    }
    private fun createImageUri(filename: String, mimeType: String): Uri? {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.DISPLAY_NAME, filename)
        values.put(MediaStore.Images.Media.MIME_TYPE,mimeType)
        val resolver = requireContext().contentResolver
        return resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
    }
    private fun newFileName(): String{
        val sdf = SimpleDateFormat("yyyyMMdd_HHmmss")
        val filename = sdf.format(System.currentTimeMillis())
        return "$filename.jpg"
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
    private fun editPlant( plantid: Int, enrolltime: String, pname: String, pdate: String, ppoint: String, plocation: String, pcycle: String, phour: Int, pminute: Int, ptemp: String, phumid: String, ptemp_alarm: Int, phumid_alarm: Int,
                                  imageurl: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                // PHP 스크립트의 URL
                val url = URL("http://10.0.2.2/editplant.php")

                // HttpURLConnection 열기
                val connection = url.openConnection() as HttpURLConnection

                // POST 요청 설정
                connection.requestMethod = "POST"
                connection.doOutput = true

                // POST 데이터 작성
                val postData = URLEncoder.encode("id", "UTF-8") + "=" + URLEncoder.encode(plantid.toString(), "UTF-8") + "&" +
                        URLEncoder.encode("currenttime", "UTF-8") + "=" + URLEncoder.encode(enrolltime, "UTF-8") + "&" +
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
                        //Fragment_Diary1 으로 넘어감
                        replaceFragment(Fragment_Album2())
                    }
                } else {
                    launch(Dispatchers.Main) {
                        AlertDialog.Builder(requireContext())
                            .setTitle("With P")
                            .setMessage("등록 실패")
                            .setPositiveButton("확인") { dialog, which -> Log.d("MyTag", "positive") }
                            .create()
                            .show()
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
    private fun absolutelyPath(path: Uri?, context: Context): String{
        var proj: Array<String> = arrayOf(MediaStore.Images.Media.DATA)
        var c: Cursor? = context.contentResolver.query(path!!, proj, null, null, null)
        var index = c?.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        c?.moveToFirst()
        var result = c?.getString(index!!)

        return result!!
    }
    // onActivityResult 메서드를 오버라이드하여 갤러리로부터 선택된 이미지를 처리
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            if (requestCode == GALLERY_REQUEST_CODE) {
                // 갤러리에서 선택한 이미지의 URI 가져오기
                selectedImageUri = data?.data

                // 선택한 이미지를 ImageView에 표시
                selectedImageUri?.let { uri ->
                    image.setImageURI(uri)
                }
            }else if(requestCode == REQ_CAMERA){
                // val imageBitmap = data?.extras?.get("data") as Bitmap?
                selectedImageUri?.let { uri ->
                    image.setImageURI(uri)
                }

            }
        }
    }
    private fun replaceFragment(fragment: Fragment){
        val transaction = requireActivity().supportFragmentManager.beginTransaction()

        val fragment = fragment
        val bundle = Bundle()
        bundle.putString("userEmail", userEmail)
        bundle.putString("plantName", plantName)
        bundle.putInt("plantId", plantId)
        bundle.putString("plantDate", plantDate)
        bundle.putString("plantPoint", plantPoint)
        bundle.putInt("plantHour", plantHour)
        bundle.putInt("plantMinute", plantMinute)
        bundle.putString("plantPlace", plantPlace)
        bundle.putString("wateringCycle", wateringCycle)
        bundle.putString("imageUrl", ImageUrl)
        bundle.putInt("wateringAlarm", wateringAlarm)
        bundle.putInt("tempHumidAlarm", tempHumidAlarm)
        bundle.putString("temperature", temperature)
        bundle.putString("humid", humid)
        bundle.putString("enrollTime", enrollTime)

        // Fragment에 Bundle을 설정
        fragment.arguments = bundle

        // FragmentTransaction을 사용하여 PlantEnrollFragment로 전환
        transaction.replace(R.id.container, fragment)
        transaction.addToBackStack(null) // 이전 Fragment로 돌아갈 수 있도록 back stack에 추가
        transaction.commit() // 변경 사항을 적용
    }
}
