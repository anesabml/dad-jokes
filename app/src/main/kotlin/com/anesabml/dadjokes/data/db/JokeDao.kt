package com.anesabml.dadjokes.data.db

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.anesabml.dadjokes.data.entity.JokeLocalEntity
import kotlinx.coroutines.flow.Flow

interface JokeDao {

    @Insert
    suspend fun insert(jokeLocalEntity: JokeLocalEntity): Long

    @Delete
    suspend fun delete(jokeLocalEntity: JokeLocalEntity): Long

    @Query("SELECT * FROM jokes")
    fun getJokes(): Flow<List<JokeLocalEntity>>
}