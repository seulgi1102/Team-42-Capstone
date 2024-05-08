package com.example.plant

class UserItem {
    private var uid: String = ""
    private var uemail: String = ""
    private var joindate: String = ""
    private var ubirth: String = ""

    fun setUid(userId: String){
        uid = userId
    }
    fun getUid(): String{
        return uid
    }
    fun setUemail(userEmail:String){
        uemail = userEmail
    }
    fun getUemail(): String{
        return uemail
    }
    fun setJoinDate(userJoinDate: String){
        joindate = userJoinDate
    }
    fun getJoinDate(): String{
        return joindate
    }
    fun setUbirth(userBirth:String){
        ubirth = userBirth
    }
    fun getUbirth(): String{
        return ubirth
    }
}

