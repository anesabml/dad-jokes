package com.anesabml.dadjokes.ui.favorites

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anesabml.dadjokes.domain.model.Joke
import com.anesabml.dadjokes.usecase.GetFavoriteJokesUseCase
import com.anesabml.dadjokes.utils.Resources
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class FavoritesViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getFavoriteJokesUseCase: GetFavoriteJokesUseCase
) : ViewModel() {

    private val _jokes = MutableStateFlow<Resources<List<Joke>>>(Resources.Loading)
    val jokes: StateFlow<Resources<List<Joke>>>
        get() = _jokes

    init {
        getFavoriteJokes()
    }

    fun getFavoriteJokes() {
        viewModelScope.launch {
            getFavoriteJokesUseCase.invoke()
                .collect {
                    _jokes.value = it
                }
        }
    }
}