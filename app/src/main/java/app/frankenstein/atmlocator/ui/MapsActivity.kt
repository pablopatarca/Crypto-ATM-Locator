package app.frankenstein.atmlocator.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import app.frankenstein.atmlocator.R
import app.frankenstein.atmlocator.databinding.ActivityMapsBinding
import app.frankenstein.atmlocator.domain.Venue
import app.frankenstein.atmlocator.ui.theme.ATMLocatorTheme
import app.frankenstein.atmlocator.ui.theme.Typography
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
@OptIn(ExperimentalMaterialApi::class)
class MapsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMapsBinding
    private val viewModel: MainViewModel by viewModels()
    private lateinit var mapHandler: MapHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)

        setContent {
            ATMLocatorTheme {
                val scope = rememberCoroutineScope()
                val selection = remember { mutableStateOf<Venue?>(null) }
                val scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed)
                BackdropScaffold(
                    scaffoldState = scaffoldState,
                    appBar = { },
                    backLayerContent = {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    scope.launch {
                                        changeScaffoldState(scaffoldState)
                                    }
                                }
                        ) {
                            AndroidView(factory = {binding.root})
                        }
                    },
                    frontLayerContent = {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .clickable {
                                    scope.launch {
                                        changeScaffoldState(scaffoldState)
                                    }
                                }
                        ) {
                            Text(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(6.dp),
                                text = stringResource(id = R.string.venues_list_title),
                                style = Typography.h5.copy(
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center
                                )
                            )
                            Divider()
                            VenuesList(
                                state = viewModel.state.collectAsState(),
                                onClick = { v ->
                                    scope.launch {
                                        changeScaffoldState(scaffoldState)
                                    }
                                    selection.value = v
                                    viewModel.itemSelected(v)
                                    mapHandler.onVenueSelected(v)
                                }
                            )
                        }
                    },
                    headerHeight = 150.dp,
                    peekHeight = 200.dp,
                    backLayerBackgroundColor = MaterialTheme.colors.background,
                    stickyFrontLayer = false
                ){
                    LaunchedEffect(Unit) {
                        delay(1000)
                        changeScaffoldState(scaffoldState)
                    }
                }
            }
        }

        mapHandler = MapHandler(viewModel)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(mapHandler)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.boundsSate.collect {
                        viewModel.getVenues(it)
                    }
                }
                launch {
                    viewModel.messages.collectLatest {
                        Snackbar.make(
                            binding.root.rootView,
                            it,
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                }
                launch {
                    viewModel.state.collect {
                        mapHandler.addMarkers(it.venues)
                    }
                }
            }
        }
    }

    private suspend fun changeScaffoldState(scaffoldState: BackdropScaffoldState){
        if(scaffoldState.isRevealed && !scaffoldState.isAnimationRunning)
            scaffoldState.conceal()
        else
            scaffoldState.reveal()
    }

    companion object {
        const val TAG = "MapsActivity"
    }
}