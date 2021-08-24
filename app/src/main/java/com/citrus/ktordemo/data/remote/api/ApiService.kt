package com.citrus.ktordemo.data.remote.api

import com.skydoves.sandwich.ApiResponse
import retrofit2.http.*


interface ApiService {

    companion object {
        const val BASE_URL = "http://192.168.0.68:8080/"
    }

    @Headers("Content-Type: application/json")
    @POST("/testApi")
    suspend fun getStoreId(
        @Body admin: Admin
    ): ApiResponse<ResultMsg>


    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("/queryTest")
    suspend fun searchById(
        @Field("pid") pid:String
    ):ApiResponse<Permission>
}