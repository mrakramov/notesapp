package com.mrakramov.notesapp.feature.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    val title: String,
    val content: String,
    val date: Long,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)