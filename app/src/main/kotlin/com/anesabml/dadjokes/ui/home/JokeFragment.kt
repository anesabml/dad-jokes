package com.anesabml.dadjokes.ui.home

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.Dimension
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.anesabml.dadjokes.R
import com.anesabml.dadjokes.databinding.FragmentJokeBinding
import com.anesabml.dadjokes.domain.model.Joke
import com.anesabml.dadjokes.extension.hide
import com.anesabml.dadjokes.extension.show
import com.anesabml.dadjokes.extension.showSnakeBar
import com.anesabml.dadjokes.extension.viewBinding
import com.anesabml.dadjokes.utils.Resources
import com.anesabml.dadjokes.viewModelFactoryGraph
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.Locale

@ExperimentalCoroutinesApi
class JokeFragment : Fragment(R.layout.fragment_joke) {

    private val binding: FragmentJokeBinding by viewBinding(FragmentJokeBinding::bind)
    private lateinit var viewModel: HomeViewModel
    private lateinit var textToSpeech: TextToSpeech

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelFactory =
            requireContext().viewModelFactoryGraph().getHomeViewModelFactory()

        viewModel = ViewModelProvider(this, viewModelFactory).get(HomeViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setHasOptionsMenu(true)

        setupObservers()
        setupListeners()
        setupTextToSpeech()
    }

    private fun setupTextToSpeech() {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val result = textToSpeech.setLanguage(Locale.UK)
                if (result == TextToSpeech.LANG_NOT_SUPPORTED or TextToSpeech.LANG_MISSING_DATA) {
                    binding.root.showSnakeBar(getString(R.string.language_not_supported))
                    binding.fabSpeak.isEnabled = false
                }
            } else {
                binding.root.showSnakeBar(getString(R.string.tts_not_initialized))
            }
        }
    }

    private fun setupListeners() {
        binding.fabSpeak.setOnClickListener {
            if (::textToSpeech.isInitialized && !textToSpeech.isSpeaking) {
                speakJoke((viewModel.joke.value as Resources.Success).data)
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
                updateFavoriteImageButton()
            }
            is Resources.Error -> {
                binding.progressBar.hide()
                showError(getString(R.string.error_try_again))
            }
        }
    }

    private fun updateFavoriteImageButton() {
        requireActivity().invalidateOptionsMenu()
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
            if (joke.joke.length > 100) {
                setTextSize(Dimension.SP, 34.0f)
            }
        }
        speakJoke(joke)
    }

    private fun speakJoke(joke: Joke) {
        val jokeEndings = resources.getStringArray(R.array.joke_endings)
        with(textToSpeech) {
            speak(
                "${joke.joke}, ${jokeEndings.random()}",
                TextToSpeech.QUEUE_FLUSH,
                null,
                joke.id
            )
            setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onError(utteranceId: String, errorCode: Int) {
                    super.onError(utteranceId, errorCode)
                    Timber.e("onError errorCode: $errorCode")
                    binding.root.showSnakeBar(getString(R.string.error_speaking_joke))
                }

                override fun onAudioAvailable(utteranceId: String, audio: ByteArray) {
                    super.onAudioAvailable(utteranceId, audio)
                    binding.audioWave.rawData = audio
                }

                override fun onDone(utteranceId: String) {}

                override fun onStart(utteranceId: String) {}

                override fun onError(utteranceId: String) {
                    Timber.e("onError utteranceId: $utteranceId")
                    binding.root.showSnakeBar(getString(R.string.error_speaking_joke))
                }
            })
        }
    }

    private fun showError(errorStr: String) {
        binding.root.showSnakeBar(errorStr)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        val iconResource =
            if ((viewModel.isFavorite.value as Resources.Success<Boolean>).data) R.drawable.ic_round_favorite else R.drawable.ic_round_favorite_border
        menu.findItem(R.id.action_favourite).setIcon(iconResource)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_retry -> {
                viewModel.getRandomJoke()
                false
            }
            R.id.action_favourite -> {
                onClickFavourite(item)
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun onClickFavourite(item: MenuItem) {
        val drawable = item.icon
        if (drawable == resources.getDrawable(R.drawable.ic_round_favorite, null)) {
            viewModel.removeJokeFromFavorite()
        } else {
            viewModel.addJokeToFavorite()
        }
    }

    override fun onDestroy() {
        textToSpeech.stop()
        textToSpeech.shutdown()
        super.onDestroy()
    }
}