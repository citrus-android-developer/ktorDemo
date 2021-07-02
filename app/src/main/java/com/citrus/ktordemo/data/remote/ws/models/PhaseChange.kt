package com.citrus.ktordemo.data.remote.ws.models

import com.citrus.ktordemo.data.remote.ws.Room
import com.citrus.ktordemo.util.Constants.TYPE_PHASE_CHANGE

data class PhaseChange(
    var phase: Room.Phase?,
    var time: Long,
    val drawingPlayer: String? = null
): BaseModel(TYPE_PHASE_CHANGE)
