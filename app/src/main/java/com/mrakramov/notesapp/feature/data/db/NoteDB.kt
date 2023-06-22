package com.mrakramov.notesapp.feature.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mrakramov.notesapp.feature.domain.model.Note

@Database(
    entities = [Note::class],
    version = 1,
    exportSchema = false
)
abstract class NoteDB : RoomDatabase() {

    abstract val dao: NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}