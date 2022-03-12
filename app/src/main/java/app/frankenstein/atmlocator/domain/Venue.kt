package app.frankenstein.atmlocator.domain

data class Venue(
    val id: Long,
    val name: String,
    val distance: Double,
    val category: String,
    val coordinate: Coordinate
)