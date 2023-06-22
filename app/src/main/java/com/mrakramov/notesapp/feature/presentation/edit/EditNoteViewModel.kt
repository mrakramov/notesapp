package com.mrakramov.notesapp.feature.presentation.edit

import android.util.Log
import androidx.compose.runtime.Immutable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mrakramov.notesapp.feature.domain.model.Note
import com.mrakramov.notesapp.feature.domain.usecase.NoteUseCase
import com.mrakramov.notesapp.utils.resultOf
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EditNoteViewModel @Inject constructor(
    private val useCase: NoteUseCase, savedStateHandle: SavedStateHandle
) : ViewModel() {

    init {
        val id = savedStateHandle.get<Int>("id") ?: -1
        loadNoteById(id)
    }

    private fun loadNoteById(id: Int) {
        if (id > -1) {
            viewModelScope.launch(IO) {
                resultOf { useCase.loadNoteById.invoke(id) }.onSuccess {
                    updateTitle(it.title)
                    updateDesc(it.content)
                    _uiState.update { it.copy(id = id) }
                }.onFailure {
                    _events.send(EditNoteEvents.Error(it.message ?: "Something went wrong !"))
                }
            }
        }
    }

    private val _uiState = MutableStateFlow(EditNoteUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = Channel<EditNoteEvents>(Channel.BUFFERED)
    val events = _events.receiveAsFlow()

    fun updateTitle(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun updateDesc(desc: String) {
        _uiState.update { it.copy(description = desc) }
    }

    fun addNote() {
        val date = System.currentTimeMillis()
        val title = _uiState.value.title.ifEmpty { "New Note" }
        val description = _uiState.value.description.ifEmpty { "Note Description" }
        val note = if (_uiState.value.id > -1) Note(
            id = _uiState.value.id, title = title, content = description, date = date
        )
        else Note(title, description, date)

        viewModelScope.launch(Dispatchers.Default) {
            resultOf {
                useCase.addNote.invoke(note)
            }.onSuccess {
                _events.send(EditNoteEvents.SuccessfullyAdded)
            }.onFailure {
                _events.send(EditNoteEvents.Error(it.message ?: "Something went wrong !"))
            }
        }
    }
}

sealed class EditNoteEvents {
    object SuccessfullyAdded : EditNoteEvents()
    data class Error(var message: String) : EditNoteEvents()
}

@Immutable
data class EditNoteUiState(
    val title: String = "", val description: String = "", val id: Int = -1
)
