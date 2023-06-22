package com.mrakramov.notesapp.feature.presentation.notes

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrakramov.notesapp.feature.domain.model.Note
import com.mrakramov.notesapp.feature.domain.usecase.NoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers.IO
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

@HiltViewModel
class NotesViewModel @Inject constructor(private val useCase: NoteUseCase) : ViewModel() {

    private val _uiState = MutableStateFlow(NotesScreenState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<NotesScreenEvents>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    init {
        loadNotes()
    }

    private fun loadNotes() {
        useCase.loadNotes()
            .flowOn(IO)
            .onStart {
                _uiState.update { it.copy(loading = true) }
            }.onEach { notes ->
                _uiState.update { it.copy(loading = false, notes = notes) }
            }
            .catch {
                _uiState.update { it.copy(loading = false) }
                _events.send(NotesScreenEvents.Error(it.message.toString()))
            }
            .launchIn(viewModelScope)
    }
}

sealed class NotesScreenEvents {
    data class Error(var message: String) : NotesScreenEvents()
}

@Stable
data class NotesScreenState(
    val loading: Boolean = false,
    val notes: List<Note> = emptyList()
)