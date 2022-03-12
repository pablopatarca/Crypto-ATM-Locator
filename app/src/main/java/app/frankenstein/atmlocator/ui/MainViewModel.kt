package app.frankenstein.atmlocator.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.frankenstein.atmlocator.domain.*
import app.frankenstein.atmlocator.utils.CoroutineDispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val poiUseCase: PoiUseCase,
    private val dispatcher: CoroutineDispatchers
) : ViewModel() {

    // MutableSharedFlow to emit an state once
    private val _messages = MutableSharedFlow<String>()
    val messages = _messages.asSharedFlow()
    private val _state = MutableStateFlow(MainState())
    val state: StateFlow<MainState> = _state.asStateFlow()
    private val _boundsSate = MutableStateFlow(Bounds(
        53.694865, 10.099891,
        53.394655, 9.757589
    ))
    val boundsSate: StateFlow<Bounds> = _boundsSate.asStateFlow()

    init {
        getVenues(boundsSate.value)
    }

    fun getVenues(bounds: Bounds){

        _boundsSate.value = bounds

        viewModelScope.launch(dispatcher.Main) {
            when(val res = poiUseCase(bounds = bounds)){
                is Success -> {
                    res.data.let {
                        _state.value = _state.value.copy(
                            venues = it
                        )
                    }
                }
                is Failure -> res.cause.message?.let {
                    _messages.emit(it)
                }
            }
        }
    }

    fun itemSelected(venue: Venue){
        _state.value = _state.value.copy(
            selected = venue
        )
    }

    fun setBounds(bounds: Bounds){
        _boundsSate.value = bounds
    }
}