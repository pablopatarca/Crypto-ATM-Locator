package app.frankenstein.atmlocator.ui

import app.frankenstein.atmlocator.domain.Venue

data class MainState(
    val venues: List<Venue> = listOf(),
    var selected: Venue? = null
)