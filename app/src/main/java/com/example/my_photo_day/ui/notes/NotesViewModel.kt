package com.example.my_photo_day.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.my_photo_day.domain.model.notes.Note
import com.example.my_photo_day.domain.repository.Success
import com.example.my_photo_day.domain.repository.notes.INotesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NotesViewModel(private val notesRepository: INotesRepository) : ViewModel() {

    private val _loading = MutableStateFlow<Boolean>(false)
    private val _error = MutableStateFlow<String?>(null)
    private val _notes = MutableStateFlow<ArrayList<Note>>(arrayListOf())

    private val _note = MutableStateFlow<Note?>(null)

    val loading: StateFlow<Boolean> = _loading
    val error: StateFlow<String?> = _error
    val notes: StateFlow<ArrayList<Note>> = _notes
    val note: StateFlow<Note?> = _note

    fun fetchNotes() {
        viewModelScope.launch {
            notesRepository.notes()
                .flowOn(Dispatchers.IO)
                .onStart {
                    _loading.value = true
                }
                .collect { result ->
                    _loading.value = false

                    when (result) {
                        is Success -> {
                            _notes.value = result.value
                            _error.value = null
                        }

                        else -> {
                            _error.value = "Ошибка при получении заметок"
                            _notes.value = arrayListOf()
                        }
                    }
                }
        }
    }

    fun add_Note(newNote: Note) {
        viewModelScope.launch {
            notesRepository.noteAdd(newNote)
                .flowOn(Dispatchers.IO)
                .onStart {
                    _loading.value = true
                }
                .collect { result ->
                    _loading.value = false
                    when (result) {
                        is Success -> {
                            _note.value = result.value
                            _error.value = null
                        }

                        else -> {
                            _error.value = "Ошибка при добавлении заметки"
                            _note.value = null
                        }
                    }
                }
        }
    }

    fun noteUpdatePosition(note: Note, pos: Int) {
        viewModelScope.launch {
            notesRepository.noteSetPosition(note, pos)
                .flowOn(Dispatchers.IO)
                .onStart {
                    _loading.value = true
                }
                .collect { result ->
                    _loading.value = false
                    when (result) {
                        true -> {
                            _error.value = null
                        }

                        false -> {
                            _error.value = "Ошибка при обновлении позиции заметки"
                        }
                    }
                }
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            notesRepository.noteDelete(note)
                .flowOn(Dispatchers.IO)
                .onStart {
                    _loading.value = true
                }
                .collect { result ->
                    _loading.value = false

                    when (result) {
                        true -> {
                            _error.value = null
                        }
                        false -> {
                            _error.value = "Ошибка при удалении заметки"
                        }
                    }
                }
        }
    }
}