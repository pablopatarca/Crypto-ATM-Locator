package app.frankenstein.atmlocator.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.math.BigDecimal

@JsonClass(generateAdapter = true)
data class VenueDto(
    @field:Json(name = "id") val id: Long,
    @field:Json(name = "name") val name: String,
    @field:Json(name = "category") val category: String,
    @field:Json(name = "lat") val latitude: BigDecimal,
    @field:Json(name = "lon") val longitude: BigDecimal,
    @field:Json(name = "created_on") val createdOn: Long
)