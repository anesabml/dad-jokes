package com.anesabml.dadjokes.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.anesabml.dadjokes.data.entity.JokeLocalEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JokeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(jokeLocalEntity: JokeLocalEntity)

    @Delete
    suspend fun delete(jokeLocalEntity: JokeLocalEntity)

    @Query("SELECT * FROM jokes")
    fun getJokes(): Flow<List<JokeLocalEntity>>
}