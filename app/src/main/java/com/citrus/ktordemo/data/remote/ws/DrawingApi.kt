package com.citrus.ktordemo.data.remote.ws

import com.citrus.ktordemo.data.remote.ws.models.BaseModel
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.flow.Flow
import retrofit2.http.Header

interface DrawingApi {

    /**connect or disconnect etc.*/
    @Receive
    fun observeEvents(): Flow<WebSocket.Event>

    /**send to Server*/
    @Send
    fun sendBaseModel(baseModel: BaseModel):Boolean

    /**Incoming message*/
    @Receive
    fun observeBaseModels():Flow<BaseModel>
}