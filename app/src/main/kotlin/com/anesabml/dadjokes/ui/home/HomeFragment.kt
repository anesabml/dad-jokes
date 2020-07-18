package com.anesabml.dadjokes.ui.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.anesabml.dadjokes.DadJokesApplication
import com.anesabml.dadjokes.R
import com.anesabml.dadjokes.databinding.FragmentHomeBinding
import com.anesabml.dadjokes.domain.model.Joke
import com.anesabml.dadjokes.extension.hide
import com.anesabml.dadjokes.extension.show
import com.anesabml.dadjokes.extension.viewBinding
import com.anesabml.dadjokes.utils.Resources
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val binding: FragmentHomeBinding by viewBinding(FragmentHomeBinding::bind)
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer =
            (requireContext().applicationContext as DadJokesApplication).appContainer

        val factory = HomeViewModelFactory(
            this,
            appContainer.getRandomJokeUseCase,
            appContainer.addJokeToFavoriteUseCase,
            appContainer.removeJokeFromFavoriteUseCase
        )

        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.joke.collect {
                updateViewState(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            homeViewModel.isFavorite.collect {
                updateFavorite(it)
            }
        }
    }

    private fun updateFavorite(resources: Resources<Boolean>) {
        when (resources) {
            Resources.Loading -> binding.progressBar.show()
            is Resources.Success -> {
                binding.progressBar.hide()
                val iconResource =
                    if (resources.data) R.drawable.ic_round_favorite else R.drawable.ic_round_favorite_border
                binding.buttonFavorite.setImageResource(iconResource)
            }
            is Resources.Error -> {
                binding.progressBar.hide()
                Snackbar.make(binding.root, R.string.error_try_again, Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateViewState(resources: Resources<Joke>) {
        when (resources) {
            Resources.Loading -> binding.progressBar.show()
            is Resources.Success -> {
                binding.progressBar.hide()
                binding.joke.text = resources.data.joke
            }
            is Resources.Error -> {
                binding.progressBar.hide()
                Snackbar.make(binding.root, R.string.error_try_again, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}