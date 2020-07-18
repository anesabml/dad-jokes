package com.anesabml.dadjokes.usecase

import com.anesabml.dadjokes.domain.datasource.RemoteDataSource
import com.anesabml.dadjokes.domain.model.Joke
import com.anesabml.dadjokes.utils.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class GetRandomJokeUseCase(private val remoteDataSource: RemoteDataSource) {

    suspend fun invoke(): Flow<Resources<Joke>> {
        return flow {
            emit(Resources.Loading)

            val joke = remoteDataSource.getRandomJoke()
            emit(Resources.Success(joke))
        }.catch { exception ->
            emit(Resources.Error(exception))
        }
    }
}