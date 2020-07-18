package com.anesabml.dadjokes.ui.favorites

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.anesabml.dadjokes.DadJokesApplication
import com.anesabml.dadjokes.R
import com.anesabml.dadjokes.databinding.FragmentFavoritesBinding
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
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private val binding: FragmentFavoritesBinding by viewBinding(FragmentFavoritesBinding::bind)
    private lateinit var viewModel: FavoritesViewModel
    private lateinit var favoritesAdapter: FavoritesRecyclerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer =
            (requireContext().applicationContext as DadJokesApplication).appContainer

        val factory = FavoritesViewModelFactory(
            this,
            appContainer.getFavoriteJokesUseCase
        )

        viewModel = ViewModelProvider(this, factory).get(FavoritesViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupObservers()
    }

    private fun setupRecyclerView() {
        with(binding.recyclerView) {
            layoutManager = LinearLayoutManager(context)
            favoritesAdapter = FavoritesRecyclerAdapter()
            adapter = favoritesAdapter
        }
    }

    private fun setupObservers() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.jokes.collect {
                updateViewState(it)
            }
        }
    }

    private fun updateViewState(resources: Resources<List<Joke>>) {
        when (resources) {
            Resources.Loading -> binding.progressBar.show()
            is Resources.Success -> {
                binding.progressBar.hide()
                favoritesAdapter.submitList(resources.data)
            }
            is Resources.Error -> {
                binding.progressBar.hide()
                Snackbar.make(binding.root, R.string.error_try_again, Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}