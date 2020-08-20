package com.anesabml.dadjokes

import android.app.Application
import android.content.Context
import com.anesabml.dadjokes.di.BaseDadJokesGraph
import com.anesabml.dadjokes.di.DadJokesGraph
import com.anesabml.dadjokes.di.DataGraph
import com.anesabml.dadjokes.di.UseCasesGraph
import com.anesabml.dadjokes.di.ViewModelFactoryGraph
import timber.log.Timber

class DadJokesApplication : Application() {

    val dependencyGraph = BaseDadJokesGraph(this)

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}

fun Context.dependencyGraph(): DadJokesGraph {
    return (this.applicationContext as DadJokesApplication).dependencyGraph
}

fun Context.dataGraph(): DataGraph {
    return this.dependencyGraph().dataGraph
}

fun Context.useCasesGraph(): UseCasesGraph {
    return this.dependencyGraph().useCasesGraph
}

fun Context.viewModelFactoryGraph(): ViewModelFactoryGraph {
    return this.dependencyGraph().viewModelFactoryGraph
}
