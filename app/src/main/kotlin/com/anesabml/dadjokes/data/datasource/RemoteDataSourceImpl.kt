package com.anesabml.dadjokes.data.datasource

import com.anesabml.dadjokes.data.api.JokesService
import com.anesabml.dadjokes.data.mapper.NetworkMapper
import com.anesabml.dadjokes.domain.datasource.RemoteDataSource
import com.anesabml.dadjokes.domain.model.Joke

class RemoteDataSourceImpl(
    private val jokesService: JokesService,
    private val networkMapper: NetworkMapper
) : RemoteDataSource {

    override suspend fun getRandomJoke(): Joke {
        val entity = jokesService.getRandomJoke()
        return networkMapper.fromEntity(entity)
    }
}