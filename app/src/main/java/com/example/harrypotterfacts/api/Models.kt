package com.example.harrypotterfacts.api

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Character(
    val id: String,
    val name: String,
    @SerializedName("alternate_names")
    val alternateNames: List<String>,
    val species: String,
    val house: String,
    val image: String,
) : Serializable

data class Spell(
    val name: String, // [cite: 35]
    val description: String, // [cite: 36]
) : Serializable
