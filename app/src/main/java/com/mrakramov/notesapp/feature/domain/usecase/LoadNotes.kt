package com.mrakramov.notesapp.feature.domain.usecase

import com.mrakramov.notesapp.feature.domain.mapper.NoteMapper
import com.mrakramov.notesapp.feature.domain.repo.NoteRepository
import kotlinx.coroutines.flow.map

class LoadNotes(
    private val repository: NoteRepository,
    private val mapper: NoteMapper
) {
    fun invoke() = repository.loadNotes().map { it.map { item -> mapper.invoke(item) } }
}