package com.anesabml.dadjokes.ui.favorites

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.anesabml.dadjokes.databinding.ItemFavoritesBinding
import com.anesabml.dadjokes.domain.model.Joke

class FavoritesRecyclerAdapter :
    ListAdapter<Joke, FavoritesRecyclerAdapter.ViewHolder>(JokeDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ViewHolder(ItemFavoritesBinding.inflate(inflater, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(private val binding: ItemFavoritesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(joke: Joke) {
            binding.joke.text = joke.joke
        }
    }
}