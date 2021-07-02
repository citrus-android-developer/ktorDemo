package com.citrus.ktordemo.data.remote.ws.models

data class PlayerData(
    val username:String,
    val isDrawing:Boolean,
    var score:Int = 0,
    var rank:Int = 0
)
