package com.anesabml.dadjokes.di

import android.app.Application
import com.anesabml.dadjokes.data.api.RetrofitClient
import com.anesabml.dadjokes.data.datasource.LocalDataSourceImpl
import com.anesabml.dadjokes.data.datasource.RemoteDataSourceImpl
import com.anesabml.dadjokes.data.db.AppDatabase
import com.anesabml.dadjokes.data.mapper.DatabaseMapper
import com.anesabml.dadjokes.data.mapper.NetworkMapper
import com.anesabml.dadjokes.usecase.AddJokeToFavoriteUseCase
import com.anesabml.dadjokes.usecase.GetFavoriteJokesUseCase
import com.anesabml.dadjokes.usecase.GetRandomJokeUseCase
import com.anesabml.dadjokes.usecase.RemoveJokeFromFavoriteUseCase

class AppContainer(application: Application) {

    private val jokesService = RetrofitClient.jokesService
    private val remoteDataSource =
        RemoteDataSourceImpl(jokesService, NetworkMapper)

    private val database = AppDatabase.getInstance(application)
    private val localDataSource =
        LocalDataSourceImpl(database, DatabaseMapper)

    val addJokeToFavoriteUseCase = AddJokeToFavoriteUseCase(localDataSource)
    val removeJokeFromFavoriteUseCase = RemoveJokeFromFavoriteUseCase(localDataSource)
    val getFavoriteJokesUseCase = GetFavoriteJokesUseCase(localDataSource)
    val getRandomJokeUseCase = GetRandomJokeUseCase(remoteDataSource)
}