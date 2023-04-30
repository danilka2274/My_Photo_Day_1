package com.example.my_photo_day.ui.pictureepic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.my_photo_day.domain.repository.nasa.INasaRepository

class EpicViewModelFactory(
    private val nasaRepository: INasaRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        EpicViewModel(nasaRepository) as T
}