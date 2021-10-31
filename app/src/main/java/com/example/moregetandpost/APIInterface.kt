package com.example.moregetandpost

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface APIInterface {
    @GET("/custom-people")
    fun getName(): Call<ArrayList<People>>

    @POST("/custom-people")
    fun addUser(@Body userData: People): Call<People>
}