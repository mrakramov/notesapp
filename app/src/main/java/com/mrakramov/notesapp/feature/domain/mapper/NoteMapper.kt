package com.mrakramov.notesapp.feature.domain.mapper

import com.mrakramov.notesapp.feature.domain.model.Note
import com.mrakramov.notesapp.feature.domain.model.NoteEntity
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
val tf = SimpleDateFormat("HH:mm", Locale.ENGLISH)

class NoteMapper @Inject constructor() {

     fun invoke(note: Note): NoteEntity {

        val d = Date(note.date)
        val today = sdf.format(Date())
        val date = sdf.format(d)
        val time = tf.format(d)

        return NoteEntity(
            title = note.title,
            content = note.content,
            id = note.id,
            date = date,
            today = date == today,
            time = time
        )
    }
}