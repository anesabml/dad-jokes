package com.anesabml.dadjokes.domain.datasource

import com.anesabml.dadjokes.domain.model.Joke
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun addToFavorite(joke: Joke)

    suspend fun removeFromFavorite(joke: Joke)

    suspend fun getAllJokes(): Flow<List<Joke>>
}