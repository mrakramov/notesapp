package com.mrakramov.notesapp.feature.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mrakramov.notesapp.feature.domain.model.Note
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao {

    @Query("SELECT * FROM note")
    fun loadNotes(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE id = :id")
    suspend fun loadNoteById(id: Int): Note

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: Note)

    @Query("DELETE FROM Note WHERE id=:id")
    suspend fun deleteNote(id: Int)

    @Query("SELECT * FROM Note WHERE title like '%' || :text  ||  '%'")
    suspend fun searchNotes(text: String): List<Note>
}