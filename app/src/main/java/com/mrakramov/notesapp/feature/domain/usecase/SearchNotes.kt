package com.mrakramov.notesapp.feature.domain.usecase

import com.mrakramov.notesapp.feature.domain.mapper.NoteMapper
import com.mrakramov.notesapp.feature.domain.repo.NoteRepository

class SearchNotes(
    private val repository: NoteRepository,
    private val mapper: NoteMapper
) {

    suspend fun invoke(text: String) = repository
        .searchNotes(text).map { mapper.invoke(it) }
}