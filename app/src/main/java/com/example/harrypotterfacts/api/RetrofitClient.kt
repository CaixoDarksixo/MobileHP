package com.example.harrypotterfacts.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://hp-api.onrender.com/api/"

    val api: HarryPotterApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()) //
            .build()
            .create(HarryPotterApi::class.java)
    }
}