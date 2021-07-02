package com.citrus.ktordemo.data.remote.ws.models

import com.citrus.ktordemo.util.Constants.TYPE_GAME_STATE

data class GameState(
    val drawingPlayer: String,
    val word: String
): BaseModel(TYPE_GAME_STATE)
