package com.anesabml.dadjokes.ui.favorites

import androidx.fragment.app.Fragment
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.anesabml.dadjokes.usecase.GetFavoriteJokesUseCase

class FavoritesViewModelFactory(
    fragment: Fragment,
    private val getFavoriteJokesUseCase: GetFavoriteJokesUseCase
) : AbstractSavedStateViewModelFactory(fragment, fragment.arguments) {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        return FavoritesViewModel(
            getFavoriteJokesUseCase
        ) as T
    }
}