package com.anesabml.dadjokes.data.entity

import com.squareup.moshi.Json

data class JokeNetworkEntity(
    @Json(name = "id")
    val id: Int,
    @Json(name = "joke")
    val joke: String,
    @Json(name = "status")
    val status: Int
)