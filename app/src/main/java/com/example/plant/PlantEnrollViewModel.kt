package com.example.plant

import androidx.lifecycle.ViewModel

class PlantEnrollViewModel: ViewModel() {
    var phumid_alarm: Int = 0
    var pwatering_alarm: Int = 0
    var userEmail: String? = null
    var pname: String = ""
    var pdate: String = ""
    var ppoint: String = ""
    var plocation: String = ""
    var pcycle: String = ""
    var phour: Int = 0
    var pminute: Int = 0
    var ptemp: String = ""
    var phumid: String = ""
}