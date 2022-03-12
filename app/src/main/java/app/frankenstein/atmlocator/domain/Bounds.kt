package app.frankenstein.atmlocator.domain

data class Bounds(
    val latNorthEast: Double,
    val lngNorthEast: Double,
    val latSouthWest: Double,
    val lngSouthWest: Double
)