package app.frankenstein.atmlocator.domain

interface PoiUseCase {
    suspend operator fun invoke(
        bounds: Bounds
    ) : Result<List<Venue>>
}