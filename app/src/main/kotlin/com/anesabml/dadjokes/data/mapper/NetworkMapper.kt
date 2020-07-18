package com.anesabml.dadjokes.data.mapper

import com.anesabml.dadjokes.data.entity.JokeNetworkEntity
import com.anesabml.dadjokes.domain.mapper.CustomMapper
import com.anesabml.dadjokes.domain.model.Joke

object NetworkMapper : CustomMapper<Joke, JokeNetworkEntity> {

    override fun toEntity(module: Joke): JokeNetworkEntity =
        JokeNetworkEntity(
            id = module.id,
            joke = module.joke,
            status = module.status
        )

    override fun fromEntity(entity: JokeNetworkEntity): Joke =
        Joke(
            id = entity.id,
            joke = entity.joke,
            status = entity.status
        )
}