package com.anesabml.dadjokes.di

import android.content.Context
import com.anesabml.dadjokes.data.api.RetrofitClient
import com.anesabml.dadjokes.data.datasource.LocalDataSourceImpl
import com.anesabml.dadjokes.data.datasource.RemoteDataSourceImpl
import com.anesabml.dadjokes.data.db.AppDatabase
import com.anesabml.dadjokes.data.mapper.DatabaseMapper
import com.anesabml.dadjokes.data.mapper.NetworkMapper
import com.anesabml.dadjokes.domain.datasource.LocalDataSource
import com.anesabml.dadjokes.domain.datasource.RemoteDataSource

interface DataGraph {
    val remoteDataSource: RemoteDataSource
    val localDataSource: LocalDataSource
}

class NetworkDataGraph(context: Context) : DataGraph {
    override val remoteDataSource: RemoteDataSource by lazy {
        RemoteDataSourceImpl(RetrofitClient.jokesService, NetworkMapper)
    }
    override val localDataSource: LocalDataSource by lazy {
        LocalDataSourceImpl(AppDatabase.getInstance(context), DatabaseMapper)
    }
}