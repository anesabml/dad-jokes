package com.anesabml.dadjokes.data.api

import com.anesabml.dadjokes.data.entity.JokeNetworkEntity
import retrofit2.http.GET

interface JokesService {

    @GET("")
    suspend fun getRandomJoke(): JokeNetworkEntity
}