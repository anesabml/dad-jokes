package com.anesabml.dadjokes.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.anesabml.dadjokes.data.entity.JokeLocalEntity

@Database(entities = [JokeLocalEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun jokeDao(): JokeDao

    companion object {

        private const val DATABASE_NAME = "dadJokes.db"

        private var instance: AppDatabase? = null

        private fun create(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()

        fun getInstance(context: Context): AppDatabase =
            (instance ?: create(context)).also { instance = it }
    }
}