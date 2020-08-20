package com.anesabml.dadjokes.di

import com.anesabml.dadjokes.usecase.AddJokeToFavoriteUseCase
import com.anesabml.dadjokes.usecase.GetFavoriteJokesUseCase
import com.anesabml.dadjokes.usecase.GetRandomJokeUseCase
import com.anesabml.dadjokes.usecase.RemoveJokeFromFavoriteUseCase

interface UseCasesGraph {
    val addJokeToFavoriteUseCase: AddJokeToFavoriteUseCase
    val removeJokeFromFavoriteUseCase: RemoveJokeFromFavoriteUseCase
    val getFavoriteJokesUseCase: GetFavoriteJokesUseCase
    val getRandomJokeUseCase: GetRandomJokeUseCase
}

class BaseUseCasesGraph(dataGraph: DataGraph) :
    UseCasesGraph {
    override val addJokeToFavoriteUseCase: AddJokeToFavoriteUseCase by lazy {
        AddJokeToFavoriteUseCase(dataGraph.localDataSource)
    }
    override val removeJokeFromFavoriteUseCase: RemoveJokeFromFavoriteUseCase by lazy {
        RemoveJokeFromFavoriteUseCase(dataGraph.localDataSource)
    }
    override val getFavoriteJokesUseCase: GetFavoriteJokesUseCase by lazy {
        GetFavoriteJokesUseCase(dataGraph.localDataSource)
    }
    override val getRandomJokeUseCase: GetRandomJokeUseCase by lazy {
        GetRandomJokeUseCase(dataGraph.remoteDataSource)
    }
}