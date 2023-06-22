package com.mrakramov.notesapp.feature.domain.usecase

import com.mrakramov.notesapp.feature.domain.model.Note
import com.mrakramov.notesapp.feature.domain.repo.NoteRepository

//here u can separate use case for all methods (add,get....)
// or open data class like: NoteUseCase(AddNote, DeleteNote)

class NoteUseCase(
    private val repository: NoteRepository
) {

    fun loadNotes() = repository.loadNotes()
    suspend fun loadNoteById(id: Int) = repository.loadNoteById(id)
    suspend fun insertNote(note: Note) = repository.insertNote(note)
    suspend fun deleteNote(note: Note) = repository.deleteNote(note)
}