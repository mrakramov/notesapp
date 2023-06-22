package com.mrakramov.notesapp.feature.domain.model

import androidx.room.PrimaryKey

data class NoteEntity(
    val title: String,
    val content: String,
    val date: String,
    val time: String,
    val id: Int,
    val today: Boolean = false
)