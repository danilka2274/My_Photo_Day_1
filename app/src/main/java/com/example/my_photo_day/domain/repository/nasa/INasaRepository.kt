package com.example.my_photo_day.domain.repository.nasa

import com.example.my_photo_day.domain.model.nasa.Apod
import com.example.my_photo_day.domain.model.nasa.Epic
import com.example.my_photo_day.domain.model.nasa.Mars
import com.example.my_photo_day.domain.repository.RepositoryResult

/****
Project Nasa Photo Day
Package softing.ubah4ukdev.nasaphotoday.domain
Created by Ivan Sheynmaer
2021.07.05
v1.0
 */
interface INasaRepository {
    //Получить астрономическое фото дня
    fun getAstronomyPictureDay(callback: (result: RepositoryResult<Apod>) -> Unit)

    //Получить фото Земли
    fun getEarthPictureDay(callback: (result: RepositoryResult<ArrayList<Epic>>) -> Unit)

    //Получить фото Марса
    fun getMarsPictureDay(
        earthDate: String,
        callback: (result: RepositoryResult<ArrayList<Mars>>) -> Unit
    )
}