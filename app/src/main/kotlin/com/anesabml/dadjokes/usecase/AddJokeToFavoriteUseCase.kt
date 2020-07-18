package com.anesabml.dadjokes.usecase

import com.anesabml.dadjokes.domain.datasource.LocalDataSource
import com.anesabml.dadjokes.domain.model.Joke
import com.anesabml.dadjokes.utils.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class AddJokeToFavoriteUseCase(private val localDataSource: LocalDataSource) {

    suspend fun invoke(joke: Joke): Flow<Resources<Boolean>> {
        return flow {
            emit(Resources.Loading)

            localDataSource.addToFavorite(joke)
            emit(Resources.Success(true))
        }.catch { exception ->
            emit(Resources.Error(exception))
        }
    }
}
