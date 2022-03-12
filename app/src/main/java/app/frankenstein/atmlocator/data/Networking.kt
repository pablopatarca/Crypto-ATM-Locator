package app.frankenstein.atmlocator.data

import app.frankenstein.atmlocator.domain.Failure
import app.frankenstein.atmlocator.domain.Result
import app.frankenstein.atmlocator.domain.Success
import retrofit2.Response

fun <T> Response<T>.getNetworkResponse(): Result<T> {
    return body()?.let {
        Success(it)
    } ?: errorBody()?.let {
        Failure(
            Exception(it.string())
        )
    } ?: Failure(
        Error("Unknown Network Error")
    )
}