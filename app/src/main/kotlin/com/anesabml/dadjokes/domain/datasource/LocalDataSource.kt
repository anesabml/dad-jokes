package com.anesabml.dadjokes.domain.datasource

import com.anesabml.dadjokes.domain.model.Joke
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    suspend fun addToFavorite(joke: Joke): Long

    suspend fun removeFromFavorite(joke: Joke): Long

    suspend fun getAllJokes(): Flow<List<Joke>>
}