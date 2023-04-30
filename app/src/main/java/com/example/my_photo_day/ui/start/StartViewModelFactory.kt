@file:Suppress("UNCHECKED_CAST")

package com.example.my_photo_day.ui.start

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class StartViewModelFactory(
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        StartViewModel() as T
}