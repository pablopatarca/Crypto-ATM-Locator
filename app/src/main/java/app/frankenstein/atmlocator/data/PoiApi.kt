package app.frankenstein.atmlocator.data

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface PoiApi {

    @GET("/api/v1/venues/")
    suspend fun getVenues(
        @Query("latNorthEast") latNorthEast: Double,
        @Query("lngNorthEast") lngNorthEast: Double,
        @Query("latSouthWest") latSouthWest: Double,
        @Query("lngSouthWest") lngSouthWest: Double
    ): Response<VenuesListDto>

}