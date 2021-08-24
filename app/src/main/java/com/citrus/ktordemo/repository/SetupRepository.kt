package com.citrus.ktordemo.repository

import com.citrus.ktordemo.data.remote.responses.BasicApiResponse
import com.citrus.ktordemo.data.remote.ws.Room
import com.citrus.ktordemo.util.Resource

interface SetupRepository {
    suspend fun createRoom(room: Room): Resource<Unit>
    suspend fun getRoom(searchQuery: String): Resource<List<Room>>
    suspend fun joinRoom(userName: String, roomName:String): Resource<Unit>
}