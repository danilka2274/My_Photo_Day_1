@file:Suppress("UNCHECKED_CAST")

package com.example.my_photo_day.ui.notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.my_photo_day.domain.repository.notes.INotesRepository

class NotesViewModelFactory(
    private val notesRepository: INotesRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        NotesViewModel(notesRepository) as T
}