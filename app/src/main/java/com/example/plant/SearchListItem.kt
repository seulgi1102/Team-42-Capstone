package com.example.plant

class SearchListItem {
    var Stitle:String =""
    var Scontent:String=""

    fun setTitle(title:String){
        Stitle = title
    }
    fun setContent(content:String){
        Scontent = content
    }
    fun getTitle():String{
        return Stitle
    }
    fun getContent():String{
        return Scontent
    }
}
