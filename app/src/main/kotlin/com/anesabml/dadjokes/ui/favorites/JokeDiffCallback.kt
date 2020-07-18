package com.anesabml.dadjokes.ui.favorites

import androidx.recyclerview.widget.DiffUtil
import com.anesabml.dadjokes.domain.model.Joke

class JokeDiffCallback : DiffUtil.ItemCallback<Joke>() {

    override fun areItemsTheSame(oldItem: Joke, newItem: Joke): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Joke, newItem: Joke): Boolean {
        return oldItem == newItem
    }
}