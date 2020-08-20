package com.anesabml.dadjokes.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.anesabml.dadjokes.ui.favorites.FavoritesViewModel
import com.anesabml.dadjokes.ui.home.HomeViewModel

interface ViewModelFactoryGraph {
    fun getHomeViewModelFactory(): ViewModelProvider.Factory
    fun getFavoritesViewModelFactory(): ViewModelProvider.Factory
}

@Suppress("UNCHECKED_CAST")
class BaseViewModelFactoryGraph(
    private val useCasesGraph: UseCasesGraph
) : ViewModelFactoryGraph {
    override fun getHomeViewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return HomeViewModel(
                    useCasesGraph.getRandomJokeUseCase,
                    useCasesGraph.addJokeToFavoriteUseCase,
                    useCasesGraph.removeJokeFromFavoriteUseCase
                ) as T
            }
        }
    }

    override fun getFavoritesViewModelFactory(): ViewModelProvider.Factory {
        return object : ViewModelProvider.Factory {
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return FavoritesViewModel(
                    useCasesGraph.getFavoriteJokesUseCase
                ) as T
            }
        }
    }
}
