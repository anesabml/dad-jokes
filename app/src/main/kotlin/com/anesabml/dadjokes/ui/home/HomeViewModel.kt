package com.anesabml.dadjokes.ui.home

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anesabml.dadjokes.domain.model.Joke
import com.anesabml.dadjokes.usecase.AddJokeToFavoriteUseCase
import com.anesabml.dadjokes.usecase.GetRandomJokeUseCase
import com.anesabml.dadjokes.usecase.RemoveJokeFromFavoriteUseCase
import com.anesabml.dadjokes.utils.Resources
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class HomeViewModel(
    private val savedStateHandle: SavedStateHandle,
    private val getRandomJokeUseCase: GetRandomJokeUseCase,
    private val addJokeToFavoriteUseCase: AddJokeToFavoriteUseCase,
    private val removeJokeFromFavoriteUseCase: RemoveJokeFromFavoriteUseCase
) : ViewModel() {

    private val _joke = MutableStateFlow<Resources<Joke>>(Resources.Loading)
    val joke: StateFlow<Resources<Joke>>
        get() = _joke

    private val _isFavorite = MutableStateFlow<Resources<Boolean>>(Resources.Success(false))
    val isFavorite: StateFlow<Resources<Boolean>>
        get() = _isFavorite

    init {
        getRandomJoke()
    }

    fun getRandomJoke() {
        viewModelScope.launch {
            getRandomJokeUseCase.invoke()
                .collect {
                    _joke.value = it
                }
        }
    }

    fun addJokeToFavorite() {
        viewModelScope.launch {
            when (val joke = _joke.value) {
                // Only proceed when there is a successful result
                is Resources.Success -> {
                    addJokeToFavoriteUseCase.invoke(joke.data)
                        .collect {
                            _isFavorite.value = it
                        }
                }
                else -> return@launch
            }
        }
    }

    fun removeJokeFromFavorite() {
        viewModelScope.launch {
            when (val joke = _joke.value) {
                // Only proceed when there is a successful result
                is Resources.Success -> {
                    removeJokeFromFavoriteUseCase.invoke(joke.data)
                        .collect {
                            _isFavorite.value = it
                        }
                }
                else -> return@launch
            }
        }
    }
}