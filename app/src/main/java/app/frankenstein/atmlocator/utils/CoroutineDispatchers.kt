package app.frankenstein.atmlocator.utils

import kotlinx.coroutines.CoroutineDispatcher

interface CoroutineDispatchers {
    val Main: CoroutineDispatcher
    val Default: CoroutineDispatcher
    val IO: CoroutineDispatcher
    val Unconfined: CoroutineDispatcher
}