package com.example.daytracker.data.network

import com.example.daytracker.data.model.User
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface UserApi {
    @POST("/login/{username}/{password}")
    suspend fun login(
        @Path("username") username: String,
        @Path("password") password: String
    ): Response<User>

    @POST("/register/{username}/{email}/{password}")
    suspend fun register(
        @Path("username") username: String,
        @Path("email") email: String,
        @Path("password") password: String
    ): Response<User>

    @GET("/user/{id}")
    suspend fun getUser(): Response<User>

    companion object {
        fun create(baseUrl: String): UserApi {
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(UserApi::class.java)
        }
    }
}