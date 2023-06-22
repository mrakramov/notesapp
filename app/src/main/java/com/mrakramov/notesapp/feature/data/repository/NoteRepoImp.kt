package com.mrakramov.notesapp.feature.data.repository

import com.mrakramov.notesapp.feature.data.db.NoteDao
import com.mrakramov.notesapp.feature.domain.model.Note
import com.mrakramov.notesapp.feature.domain.repo.NoteRepository
import kotlinx.coroutines.flow.Flow

class NoteRepoImp(
    private val dao: NoteDao
) : NoteRepository {

    override fun loadNotes(): Flow<List<Note>> = dao.loadNotes()
    override suspend fun searchNotes(text: String): List<Note> = dao.searchNotes(text)

    override suspend fun loadNoteById(id: Int): Note = dao.loadNoteById(id)

    override suspend fun insertNote(note: Note) = dao.insertNote(note)

    override suspend fun deleteNote(id: Int) = dao.deleteNote(id)
}