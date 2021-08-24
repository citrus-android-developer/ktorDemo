package com.citrus.ktordemo.data.remote.api

data class Admin(
    val id:Int,
    val emp:String,
    val storeId:String
)


data class ResultMsg(
    val successful:String,
    val message:String
)