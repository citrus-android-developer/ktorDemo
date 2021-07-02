package com.citrus.ktordemo.data.remote.ws.models

import com.citrus.ktordemo.util.Constants.TYPE_PLAYERS_LIST

data class PlayersList(
    val players: List<PlayerData>
): BaseModel(TYPE_PLAYERS_LIST)
