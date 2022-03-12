package app.frankenstein.atmlocator.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class VenuesListDto(
    @field:Json(name = "venues") val venues: List<VenueDto>
)