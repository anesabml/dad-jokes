package com.anesabml.dadjokes.usecase

import com.anesabml.dadjokes.domain.datasource.LocalDataSource
import com.anesabml.dadjokes.domain.model.Joke
import com.anesabml.dadjokes.utils.Resources
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import timber.log.Timber

class GetFavoriteJokesUseCase(private val localDataSource: LocalDataSource) {

    suspend fun invoke(): Flow<Resources<List<Joke>>> {
        return flow {
            emit(Resources.Loading)

            localDataSource.getAllJokes().collect {
                emit(Resources.Success(it))
            }
        }.catch { exception ->
            Timber.e(exception)
            emit(Resources.Error(exception))
        }
    }
}