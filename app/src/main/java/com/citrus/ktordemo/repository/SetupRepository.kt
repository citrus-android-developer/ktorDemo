package com.citrus.ktordemo.repository

import com.citrus.ktordemo.data.remote.api.Admin
import com.citrus.ktordemo.data.remote.api.ResultMsg
import com.citrus.ktordemo.data.remote.responses.BasicApiResponse
import com.citrus.ktordemo.data.remote.ws.Room
import com.citrus.ktordemo.util.Resource
import kotlinx.coroutines.flow.Flow

interface SetupRepository {
    suspend fun createRoom(room: Room): Resource<Unit>
    suspend fun getRoom(searchQuery: String): Resource<List<Room>>
    suspend fun joinRoom(userName: String, roomName:String): Resource<Unit>
}