package app.frankenstein.atmlocator.data

import app.frankenstein.atmlocator.domain.Bounds
import app.frankenstein.atmlocator.domain.Result
import app.frankenstein.atmlocator.domain.Venue

interface Repository {

    suspend fun getPoi(
        bounds: Bounds
    ) : Result<List<Venue>>

}