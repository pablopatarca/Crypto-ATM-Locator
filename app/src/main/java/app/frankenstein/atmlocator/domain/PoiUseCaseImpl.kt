package app.frankenstein.atmlocator.domain

import app.frankenstein.atmlocator.data.Repository
import app.frankenstein.atmlocator.utils.CoroutineDispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PoiUseCaseImpl @Inject constructor(
    private val repository: Repository,
    private val dispatcher: CoroutineDispatchers
) : PoiUseCase {

    override suspend operator fun invoke(
        bounds: Bounds
    ) : Result<List<Venue>> {
        return withContext(dispatcher.IO){
            when(val result = repository.getPoi(bounds = bounds)){
                is Success -> {
                    Success(result.data.sortedBy { it.name })
                }
                is Failure -> {
                    result
                }
            }
        }
    }
}