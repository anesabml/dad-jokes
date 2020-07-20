package com.anesabml.dadjokes.ui.home

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.anesabml.dadjokes.DadJokesApplication
import com.anesabml.dadjokes.R
import com.anesabml.dadjokes.databinding.FragmentJokeBinding
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
class JokeFragment : Fragment(R.layout.fragment_joke) {

    private val binding: FragmentJokeBinding by viewBinding(FragmentJokeBinding::bind)
    private lateinit var viewModel: HomeViewModel

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

        viewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupObservers()
        setupListeners()
    }

    private fun setupListeners() {
        binding.buttonFavorite.setOnClickListener {
            val tag = it.tag as Boolean
            if (tag) {
                viewModel.removeJokeFromFavorite()
            } else {
                viewModel.addJokeToFavorite()
            }
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.joke.collect {
                updateViewState(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isFavorite.collect {
                updateFavorite(it)
            }
        }
    }

    private fun updateFavorite(resources: Resources<Boolean>) {
        when (resources) {
            Resources.Loading -> binding.progressBar.show()
            is Resources.Success -> {
                binding.progressBar.hide()
                updateFavoriteImageButton(resources.data)
            }
            is Resources.Error -> {
                binding.progressBar.hide()
                showError(getString(R.string.error_try_again))
            }
        }
    }

    private fun updateFavoriteImageButton(isFavorite: Boolean) {
        val iconResource =
            if (isFavorite) R.drawable.ic_round_favorite else R.drawable.ic_round_favorite_border
        with(binding.buttonFavorite) {
            setImageResource(iconResource)
            tag = isFavorite
        }
    }

    private fun updateViewState(resources: Resources<Joke>) {
        when (resources) {
            Resources.Loading -> binding.progressBar.show()
            is Resources.Success -> {
                binding.progressBar.hide()
                showJoke(resources.data)
            }
            is Resources.Error -> {
                binding.progressBar.hide()
                showError(getString(R.string.error_try_again))
            }
        }
    }

    private fun showJoke(joke: Joke) {
        with(binding.joke) {
            text = joke.joke
            if (joke.joke.length > 80) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    setTextAppearance(R.style.TextAppearance_MaterialComponents_Headline4)
                } else {
                    setTextAppearance(
                        context,
                        R.style.TextAppearance_MaterialComponents_Headline4
                    )
                }
            }
        }
    }

    private fun showError(string: String) {
        Snackbar.make(binding.root, string, Snackbar.LENGTH_SHORT).show()
    }
}