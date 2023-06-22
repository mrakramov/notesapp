package com.mrakramov.notesapp.feature.domain.usecase

import com.mrakramov.notesapp.feature.domain.model.Note
import com.mrakramov.notesapp.feature.domain.repo.NoteRepository

class AddNote(private val repository: NoteRepository){
    suspend fun invoke(note: Note) = repository.insertNote(note)
}
