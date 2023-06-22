package com.mrakramov.notesapp.di

import com.mrakramov.notesapp.feature.data.db.NoteDB
import com.mrakramov.notesapp.feature.data.repository.NoteRepoImp
import com.mrakramov.notesapp.feature.domain.mapper.NoteMapper
import com.mrakramov.notesapp.feature.domain.repo.NoteRepository
import com.mrakramov.notesapp.feature.domain.usecase.AddNote
import com.mrakramov.notesapp.feature.domain.usecase.DeleteNote
import com.mrakramov.notesapp.feature.domain.usecase.LoadNoteById
import com.mrakramov.notesapp.feature.domain.usecase.LoadNotes
import com.mrakramov.notesapp.feature.domain.usecase.NoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object ViewModelModule {

    @Provides
    fun provideNoteRepository(db: NoteDB): NoteRepository {
        return NoteRepoImp(db.dao)
    }

    @Provides
    fun provideNoteMapper(): NoteMapper {
        return NoteMapper()
    }

    @Provides
    fun provideNoteUseCases(
        repository: NoteRepository,
        mapper: NoteMapper
    ): NoteUseCase {
        return NoteUseCase(
            addNote = AddNote(repository),
            deleteNote = DeleteNote(repository),
            loadNoteById = LoadNoteById(repository),
            loadNotes = LoadNotes(repository, mapper)
        )
    }
}