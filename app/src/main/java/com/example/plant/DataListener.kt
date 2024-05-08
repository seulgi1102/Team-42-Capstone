package com.example.plant

interface DataListener {
    fun onNameReceived(plantName: String)
    fun onPlantInfoReceived(registrationDate: String, characteristics: String, location: String)

    // 프래그먼트 3에서 물 주는 주기 정보 전달
    fun onWateringInfoReceived(wateringHour: Int, wateringMinute: Int, lastWateringDate: String, temperature: String, humidity: String)
}