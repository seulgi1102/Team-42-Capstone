package com.example.plant

class DiaryListItem {
    private var itemId: Int = 0
    private var plantId: Int = 0
    private var plantName: String = ""
    private var userEmail: String = ""
    private var diaryDate: String = ""
    private var diaryTitle: String = ""
    private var diaryContent: String = ""

    fun setItemId(id: Int){
        itemId = id
    }
    fun getItemId(): Int{
        return itemId
    }
    fun setPlantId(id: Int){
        plantId = id
    }
    fun getPlantId(): Int{
        return itemId
    }

    fun setUserEmail(email: String) {
        userEmail = email
    }

    fun getUserEmail(): String {
        return userEmail
    }

    fun setPlantName(name: String) {
        plantName = name
    }

    fun getPlantName(): String {
        return plantName
    }

    fun setDiaryDate(date: String) {
        diaryDate = date
    }

    fun getDiaryDate(): String {
        return diaryDate
    }

    fun setDiaryTitle(title: String) {
        diaryTitle = title
    }

    fun getDiaryTitle(): String {
        return diaryTitle
    }

    fun setDiaryContent(content: String) {
        diaryContent = content
    }
    fun getDiaryContent(): String {
        return diaryContent
    }

}
