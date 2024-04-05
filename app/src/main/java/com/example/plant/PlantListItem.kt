package com.example.plant

import android.view.View
import android.widget.ImageView
import android.widget.ListView

class PlantListItem {
    private var plantName = ""
    private var plantImgSrc: Int = R.drawable.img_2
    private var itemNum = 0
    fun setName(name:String){
        plantName = name
    }
    fun getName():String{
        return plantName
    }
    fun setImgSrc(resourceId: Int) {
        plantImgSrc = resourceId
    }
    fun setNum(itemNumber: Int){
        itemNum = itemNumber
    }
    fun getNum():Int{
        return itemNum
    }
    fun getImgSrc(): Int {
        return plantImgSrc
    }
}