package com.mrakramov.notesapp.di

import android.app.Application
import androidx.room.Room
import com.mrakramov.notesapp.feature.data.db.NoteDB
import com.mrakramov.notesapp.feature.data.repository.NoteRepoImp
import com.mrakramov.notesapp.feature.domain.repo.NoteRepository
import com.mrakramov.notesapp.feature.domain.usecase.NoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(app: Application): NoteDB {
        return Room.databaseBuilder(
            app,
            NoteDB::class.java,
            NoteDB.DATABASE_NAME
        ).build()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(db: NoteDB): NoteRepository {
        return NoteRepoImp(db.dao)
    }

    @Provides
    @Singleton
    fun provideNoteUseCases(repository: NoteRepository): NoteUseCase {
        return NoteUseCase(repository)
    }

}