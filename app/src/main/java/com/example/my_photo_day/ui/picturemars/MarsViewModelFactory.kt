@file:Suppress("UNCHECKED_CAST")

package com.example.my_photo_day.ui.picturemars

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.my_photo_day.domain.repository.nasa.INasaRepository

class MarsViewModelFactory(
    private val nasaRepository: INasaRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        MarsViewModel(nasaRepository) as T
}