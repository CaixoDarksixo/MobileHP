package com.example.harrypotterfacts.api

import retrofit2.http.GET
import retrofit2.http.Path

interface HarryPotterApi {
    @GET("character/{id}")
    suspend fun getCharacterById(
        @Path("id") id: String,
    ): List<Character>

    @GET("characters/staff")
    suspend fun getStaff(): List<Character>

    @GET("characters/house/{house}")
    suspend fun getStudentsByHouse(
        @Path("house") house: String,
    ): List<Character>

    @GET("spells")
    suspend fun getSpells(): List<Spell>
}
