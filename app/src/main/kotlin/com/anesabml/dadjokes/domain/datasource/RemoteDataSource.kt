package com.anesabml.dadjokes.domain.datasource

import com.anesabml.dadjokes.domain.model.Joke

interface RemoteDataSource {

    suspend fun getRandomJoke(): Joke
}