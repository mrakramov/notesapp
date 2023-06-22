package com.mrakramov.notesapp.feature.domain.usecase

import com.mrakramov.notesapp.feature.domain.repo.NoteRepository

class LoadNoteById (private val repository: NoteRepository) {
    suspend fun invoke(id: Int) = repository.loadNoteById(id)

}