package com.mrakramov.notesapp.feature.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mrakramov.notesapp.feature.domain.model.Note

@Database(
    entities = [Note::class], version = 1, exportSchema = false
)
abstract class NoteDB : RoomDatabase() {

    internal abstract val dao: NoteDao

    companion object {
        const val DATABASE_NAME = "notes_db"
    }
}

@Synchronized
fun getDB(context: Context) = Room.databaseBuilder(
    context, NoteDB::class.java, NoteDB.DATABASE_NAME
).build()