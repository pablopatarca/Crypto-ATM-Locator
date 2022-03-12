package app.frankenstein.atmlocator.domain

sealed class Result<T>
data class Success<T>(val data: T) : Result<T>()
data class Failure<T>(val cause: Throwable) : Result<T>()
