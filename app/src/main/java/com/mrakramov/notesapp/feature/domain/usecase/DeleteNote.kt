package com.mrakramov.notesapp.feature.domain.usecase

import com.mrakramov.notesapp.feature.domain.model.Note
import com.mrakramov.notesapp.feature.domain.repo.NoteRepository

class DeleteNote(private val repository: NoteRepository) {
    suspend fun invoke(note: Note) = repository.deleteNote(note)
}