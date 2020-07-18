package com.anesabml.dadjokes.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "jokes")
data class JokeLocalEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    @ColumnInfo(name = "joke")
    val joke: String,
    @ColumnInfo(name = "status")
    val status: Int
)