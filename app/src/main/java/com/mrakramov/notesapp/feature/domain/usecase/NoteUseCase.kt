package com.mrakramov.notesapp.feature.domain.usecase

import com.mrakramov.notesapp.feature.domain.model.Note
import com.mrakramov.notesapp.feature.domain.repo.NoteRepository

//here u can separate use case for all methods (add,get....)
// or open data class like: NoteUseCase(AddNote, DeleteNote)

data class NoteUseCase(
    val addNote: AddNote,
    val deleteNote: DeleteNote,
    val loadNoteById: LoadNoteById,
    val loadNotes: LoadNotes,
    val searchNote:SearchNotes
)