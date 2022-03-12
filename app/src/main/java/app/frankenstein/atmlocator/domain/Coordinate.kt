package app.frankenstein.atmlocator.domain

import java.math.BigDecimal

data class Coordinate(
    val lat: BigDecimal,
    val lng: BigDecimal
)