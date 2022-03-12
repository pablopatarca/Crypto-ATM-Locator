package app.frankenstein.atmlocator.data

import app.frankenstein.atmlocator.domain.*
import app.frankenstein.atmlocator.utils.CoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val poiDataSource: PoiApi,
    private val dispatchers: CoroutineDispatchers
) : Repository {

    override suspend fun getPoi(
        bounds: Bounds
    ): Result<List<Venue>> {
        return try {
            poiDataSource.getVenues(
                bounds.latNorthEast,
                bounds.lngNorthEast,
                bounds.latSouthWest,
                bounds.lngSouthWest
            ).getNetworkResponse().let { result ->

                withContext(dispatchers.Default){
                    when(result){
                        is Success -> {
                            Success(
                                result.toVenuesList()
                            )
                        }
                        is Failure -> Failure(result.cause)
                    }
                }
            }
        } catch (e: Exception){
            Failure(e)
        }
    }

    private fun Success<VenuesListDto>.toVenuesList(): List<Venue>{
        return data.venues.map {
            with(it){
                Venue(
                    id = id,
                    name = name,
                    category = category,
                    coordinate = Coordinate(
                        latitude,
                        longitude
                    )
                )
            }
        }
    }
}