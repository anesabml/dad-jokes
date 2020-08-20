package com.anesabml.dadjokes.di

import android.content.Context

interface DadJokesGraph {
    val dataGraph: DataGraph
    val useCasesGraph: UseCasesGraph
    val viewModelFactoryGraph: ViewModelFactoryGraph
}

class BaseDadJokesGraph(context: Context) : DadJokesGraph {
    override val dataGraph: DataGraph = NetworkDataGraph(context)
    override val useCasesGraph: UseCasesGraph = BaseUseCasesGraph(dataGraph)
    override val viewModelFactoryGraph: ViewModelFactoryGraph =
        BaseViewModelFactoryGraph(useCasesGraph)
}