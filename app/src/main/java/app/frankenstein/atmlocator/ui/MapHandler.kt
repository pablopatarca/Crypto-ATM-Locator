package app.frankenstein.atmlocator.ui

import android.util.Log
import app.frankenstein.atmlocator.R
import app.frankenstein.atmlocator.domain.Bounds
import app.frankenstein.atmlocator.domain.Venue
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*

class MapHandler(
    private val viewModel: MainViewModel
) : OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private var cameraAutoUpdate: Boolean = false
    private val markersMap = HashMap<Long, Marker>()
    private var cameraReady: Boolean = false

    private val atmIcon = BitmapDescriptorFactory.fromResource(R.drawable.marker_atm)

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setOnMarkerClickListener {
            cameraAutoUpdate = true
            false
        }

        mMap.setOnCameraIdleListener {

            if(cameraAutoUpdate){
                cameraAutoUpdate = false
                return@setOnCameraIdleListener
            }

            val currentBounds = mMap.projection
                .visibleRegion.latLngBounds.toBounds()

            Log.d(MapsActivity.TAG, "Visible Region: $currentBounds")

            viewModel.setBounds(currentBounds)
        }

        setCamera(viewModel.boundsSate.value)

        cameraReady = true
    }

    fun addMarkers(venues: List<Venue>){
        if(!cameraReady) return
        mMap.clear()
        markersMap.clear()
        venues.forEach { v ->
            mMap.addMarker(
                MarkerOptions()
                    .title(v.category)
                    .icon(atmIcon)
                    .position(
                        LatLng(
                            v.coordinate.lat.toDouble(),
                            v.coordinate.lng.toDouble()
                        )
                    )
            )?.let {
                markersMap[v.id] = it
            }
        }
    }

    private fun setCamera(bounds: Bounds){
        val cameraUpdate = CameraUpdateFactory
            .newLatLngBounds(bounds.toLatLngBounds(),10)
        mMap.moveCamera(cameraUpdate)
        Log.d(MapsActivity.TAG, "Set at ${bounds.toLatLngBounds()}")
    }

    fun onVenueSelected(venue: Venue) {
        markersMap[venue.id]?.let {
            cameraAutoUpdate = true
            it.showInfoWindow()
            mMap.animateCamera(
                CameraUpdateFactory.newLatLngZoom(it.position, 15f)
            )
        }
    }

    private fun LatLngBounds.toBounds(): Bounds {
        return Bounds(
            latNorthEast = this.northeast.latitude,
            lngNorthEast = this.northeast.longitude,
            latSouthWest = this.southwest.latitude,
            lngSouthWest = this.southwest.longitude,
        )
    }

    private fun Bounds.toLatLngBounds(): LatLngBounds {
        return LatLngBounds(
            LatLng(latSouthWest, lngSouthWest),
            LatLng(latNorthEast, lngNorthEast)
        )
    }

}