package com.mrakramov.notesapp.feature.domain.repo

import com.mrakramov.notesapp.feature.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    fun loadNotes(): Flow<List<Note>>

    suspend fun loadNoteById(id: Int): Note

    suspend fun insertNote(note: Note)

    suspend fun deleteNote(note: Note)
}