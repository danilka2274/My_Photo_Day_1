@file:Suppress("UNCHECKED_CAST")

package com.example.my_photo_day.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.my_photo_day.domain.storage.AppThemeStorage

class SettingsViewModelFactory(
    private val themeStorage: AppThemeStorage
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        SettingsViewModel(themeStorage) as T
}