package com.example.flpcurrentlocation

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource

class CurrentLocationComponent(
    context: Context,
    private val locationSuccessCallback: (Location) -> Unit,
    private val locationErrorCallback: (String) -> Unit
) : LifecycleObserver {

    private val fusedLocationProviderClient by lazy {
        LocationServices.getFusedLocationProviderClient(context)
    }
    private var cancellationTokenSource = CancellationTokenSource()

    @SuppressLint("MissingPermission")
    fun getCurrentLocation() {
        // getCurrentLocation method is used to get a single latest location by adding a listener.
        // This should be used when you want to get only one location instead of continuous stream
        // of locations.
        fusedLocationProviderClient.getCurrentLocation(
            LocationRequest.PRIORITY_HIGH_ACCURACY,
            cancellationTokenSource.token
        ).addOnSuccessListener { location ->
            if (location != null) {
                locationSuccessCallback(location)
            } else {
                locationErrorCallback("Location not found")
            }
        }.addOnFailureListener { exception ->
            exception.localizedMessage?.let {
                locationErrorCallback(it)
            }
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onStop() {
        locationErrorCallback("Location request cancelled")

        //cancel ongoing location request
        cancellationTokenSource.cancel()

        //re-initialize new cancellation token source
        cancellationTokenSource = CancellationTokenSource()
    }

}