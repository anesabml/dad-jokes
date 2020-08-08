package com.anesabml.dadjokes.ui.home

import android.os.Build
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
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
import com.anesabml.dadjokes.extension.showSnakeBar
import com.anesabml.dadjokes.extension.viewBinding
import com.anesabml.dadjokes.utils.Resources
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
                } /*else {
                    textToSpeech.voices.forEach {
                        Timber.i(it.name)
                    }
                }*/
            } else {
                binding.root.showSnakeBar(getString(R.string.tts_not_initialized))
            }
        }
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

                override fun onAudioAvailable(utteranceId: String, audio: ByteArray?) {
                    super.onAudioAvailable(utteranceId, audio)
                    binding.wave.setRawAudioBytes(audio)
                }

                override fun onDone(utteranceId: String) {
                    binding.wave.hide()
                }

                override fun onStart(utteranceId: String) {
                    binding.wave.show()
                }

                override fun onError(utteranceId: String) {
                    Timber.e("onError utteranceId: $utteranceId")
                    binding.wave.hide()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_retry -> {
                viewModel.getRandomJoke()
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroy() {
        textToSpeech.stop()
        textToSpeech.shutdown()
        super.onDestroy()
    }
}