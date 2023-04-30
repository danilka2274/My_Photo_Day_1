package com.example.my_photo_day.ui.pictureapod

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.my_photo_day.domain.repository.nasa.INasaRepository

class ApodViewModelFactory(
    private val nasaRepository: INasaRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ApodViewModel(nasaRepository) as T
}