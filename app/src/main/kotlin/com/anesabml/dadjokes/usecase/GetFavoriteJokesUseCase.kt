package com.anesabml.dadjokes.usecase

import com.anesabml.dadjokes.domain.datasource.LocalDataSource
import com.anesabml.dadjokes.domain.model.Joke
import kotlinx.coroutines.flow.Flow

class GetFavoriteJokesUseCase(private val localDataSource: LocalDataSource) {

    suspend fun invoke(): Flow<List<Joke>> {
        // TODO: 7/18/20
        return localDataSource.getAllJokes()
    }
}