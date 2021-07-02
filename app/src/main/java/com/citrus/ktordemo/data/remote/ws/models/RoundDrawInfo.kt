package com.citrus.ktordemo.data.remote.ws.models

import com.citrus.ktordemo.util.Constants.TYPE_CUR_ROUND_DRAW_INFO

data class RoundDrawInfo(
    val data: List<String>
): BaseModel(TYPE_CUR_ROUND_DRAW_INFO)
