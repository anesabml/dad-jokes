package com.anesabml.dadjokes.data.mapper

import com.anesabml.dadjokes.data.entity.JokeLocalEntity
import com.anesabml.dadjokes.domain.mapper.CustomMapper
import com.anesabml.dadjokes.domain.model.Joke

object DatabaseMapper : CustomMapper<Joke, JokeLocalEntity> {

    override fun toEntity(module: Joke): JokeLocalEntity =
        JokeLocalEntity(
            id = module.id,
            joke = module.joke,
            status = module.status
        )

    override fun fromEntity(entity: JokeLocalEntity): Joke =
        Joke(
            id = entity.id,
            joke = entity.joke,
            status = entity.status
        )
}