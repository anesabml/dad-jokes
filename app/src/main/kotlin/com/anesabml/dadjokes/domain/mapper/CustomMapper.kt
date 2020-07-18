package com.anesabml.dadjokes.domain.mapper

interface CustomMapper<Module, Entity> {

    fun toEntity(module: Module): Entity

    fun fromEntity(entity: Entity): Module
}