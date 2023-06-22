package com.mrakramov.notesapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.mrakramov.notesapp.feature.data.db.NoteDB
import com.mrakramov.notesapp.feature.data.db.getDB
import com.mrakramov.notesapp.feature.data.repository.NoteRepoImp
import com.mrakramov.notesapp.feature.domain.repo.NoteRepository
import com.mrakramov.notesapp.feature.domain.usecase.NoteUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideNoteDatabase(@ApplicationContext context: Context): NoteDB {
        return getDB(context)
    }
}