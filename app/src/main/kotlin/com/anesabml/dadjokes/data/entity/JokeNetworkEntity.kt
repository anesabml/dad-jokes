package com.anesabml.dadjokes.data.entity

import com.squareup.moshi.Json

data class JokeNetworkEntity(
    @Json(name = "id")
    val id: String,
    @Json(name = "joke")
    val joke: String,
    @Json(name = "status")
    val status: Int
)