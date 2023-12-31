package com.mrakramov.notesapp.feature.presentation.notes

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrakramov.notesapp.feature.domain.model.Note
import com.mrakramov.notesapp.feature.domain.model.NoteEntity
import com.mrakramov.notesapp.feature.domain.usecase.NoteUseCase
import com.mrakramov.notesapp.utils.resultOf
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class NotesViewModel @Inject constructor(private val useCase: NoteUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(NotesScreenState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<NotesScreenEvents>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    private var job: Job? = null

    init {
        loadNotes()
    }

    fun updateSearch(text: String) {
        job?.cancel()
        job = viewModelScope.launch(Dispatchers.Default) {
            _uiState.update { it.copy(search = text) }
            resultOf {
                useCase.searchNote.invoke(text)
            }.onSuccess { notes ->
                _uiState.update { it.copy(notes = notes) }
            }.onFailure {

            }
        }
    }

    private fun loadNotes() {
        useCase.loadNotes.invoke().flowOn(IO).onStart {
            _uiState.update {
                it.copy(
                    loading = true, notesCount = "Notes"
                )
            }
        }.onEach { notes ->
            val count = if (notes.isNotEmpty()) "${notes.size} Notes" else "No Notes"

            _uiState.update {
                it.copy(
                    loading = false, notes = notes, notesCount = count
                )
            }
        }.catch {
            _uiState.update { it.copy(loading = false) }
            _events.send(NotesScreenEvents.Error(it.message.toString()))
        }.launchIn(viewModelScope)
    }

    fun deleteNote(id: Int) {
        viewModelScope.launch(IO) {
            resultOf {
                useCase.deleteNote.invoke(id)
            }.onSuccess { }.onFailure { }
        }
    }
}

sealed class NotesScreenEvents {
    data class Error(var message: String) : NotesScreenEvents()
}

@Stable
data class NotesScreenState(
    val loading: Boolean = false,
    val notes: List<NoteEntity> = emptyList(),
    val notesCount: String = "",
    val search: String = ""
) {
}