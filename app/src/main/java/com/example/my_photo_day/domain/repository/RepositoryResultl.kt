package com.example.my_photo_day.domain.repository

sealed class RepositoryResult<T>
data class Success<T>(val value: T) : RepositoryResult<T>()
data class Error<T>(val value: Throwable) : RepositoryResult<T>()