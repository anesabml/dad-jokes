package com.anesabml.dadjokes.data.datasource

import com.anesabml.dadjokes.data.db.AppDatabase
import com.anesabml.dadjokes.data.mapper.DatabaseMapper
import com.anesabml.dadjokes.domain.datasource.LocalDataSource
import com.anesabml.dadjokes.domain.model.Joke
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LocalDataSourceImpl(
    private val database: AppDatabase,
    private val databaseMapper: DatabaseMapper
) : LocalDataSource {

    override suspend fun addToFavorite(joke: Joke) {
        val jokeLocalEntity = databaseMapper.toEntity(joke)
        return database.jokeDao().insert(jokeLocalEntity)
    }

    override suspend fun removeFromFavorite(joke: Joke) {
        val jokeLocalEntity = databaseMapper.toEntity(joke)
        return database.jokeDao().delete(jokeLocalEntity)
    }

    override suspend fun getAllJokes(): Flow<List<Joke>> {
        return database.jokeDao().getJokes().map {
            it.map { entity -> databaseMapper.fromEntity(entity) }
        }
    }
}
