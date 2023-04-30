package com.example.my_photo_day.ui.pictureapod

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.my_photo_day.domain.model.nasa.Apod
import com.example.my_photo_day.domain.repository.Error
import com.example.my_photo_day.domain.repository.Success
import com.example.my_photo_day.domain.repository.nasa.INasaRepository

class ApodViewModel(private val nasaRepository: INasaRepository) : ViewModel() {

    private val _loadingLiveData = MutableLiveData(false)
    private val _errorLiveData = MutableLiveData<String?>()
    private val _apodLiveData = MutableLiveData<Apod>()

    val loadingLiveData: LiveData<Boolean> = _loadingLiveData
    val errorLiveData: LiveData<String?> = _errorLiveData
    val apodLiveData: LiveData<Apod> = _apodLiveData

    fun getPhoto() {
        _loadingLiveData.value = true
        nasaRepository.getAstronomyPictureDay() {
            when (it) {
                is Success -> {
                    _apodLiveData.value = it.value
                    _errorLiveData.value = null
                    _loadingLiveData.value = false
                }
                is Error -> {
                    _errorLiveData.value = it.value.message.toString()
                    _loadingLiveData.value = false
                }
            }
        }
    }
}