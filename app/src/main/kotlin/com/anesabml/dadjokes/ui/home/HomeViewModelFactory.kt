package com.anesabml.dadjokes.ui.home

import androidx.fragment.app.Fragment
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.anesabml.dadjokes.usecase.AddJokeToFavoriteUseCase
import com.anesabml.dadjokes.usecase.GetRandomJokeUseCase
import com.anesabml.dadjokes.usecase.RemoveJokeFromFavoriteUseCase

class HomeViewModelFactory(
    fragment: Fragment,
    private val getRandomJokeUseCase: GetRandomJokeUseCase,
    private val addJokeToFavoriteUseCase: AddJokeToFavoriteUseCase,
    private val removeJokeFromFavoriteUseCase: RemoveJokeFromFavoriteUseCase
) : AbstractSavedStateViewModelFactory(fragment, fragment.arguments) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return HomeViewModel(
            getRandomJokeUseCase,
            addJokeToFavoriteUseCase,
            removeJokeFromFavoriteUseCase
        ) as T
    }
}