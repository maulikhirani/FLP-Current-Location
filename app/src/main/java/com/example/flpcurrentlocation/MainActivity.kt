package com.example.flpcurrentlocation

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.example.flpcurrentlocation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val locationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            // the callback gives the Map of Permission and isGranted (Boolean) state

            if (it.values.contains(false)) {
                //do this if any of the requested permissions are denied
                binding.tvCurrentLocation.text = getString(R.string.permission_not_granted)
                checkShouldShowSettingsDialog()
            } else {
                getCurrentLocation()
            }
        }

    private fun checkShouldShowSettingsDialog() {
        if (!ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            // Build intent that displays the App settings screen.
            val intent = Intent()
            intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            val uri = Uri.fromParts(
                "package",
                BuildConfig.APPLICATION_ID,
                null
            )
            intent.data = uri
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

            showToast(getString(R.string.allow_permission_from_settings))
        }
    }

    private lateinit var currentLocationComponent: CurrentLocationComponent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.lifecycleOwner = this

        currentLocationComponent = CurrentLocationComponent(this,
            {
                binding.tvCurrentLocation.text =
                    getString(R.string.current_location, it.latitude, it.longitude)
            },
            {
                binding.tvCurrentLocation.text = it
            }
        )
        lifecycle.addObserver(currentLocationComponent)

        binding.btnCurrentLocation.setOnClickListener {
            checkLocationPermission()
        }
    }

    private fun checkLocationPermission() {
        if (hasPermission(Manifest.permission.ACCESS_FINE_LOCATION) &&
            hasPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
        ) {
            getCurrentLocation()
        } else {
            //use location permission launcher to request permissions
            locationPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun getCurrentLocation() {
        binding.tvCurrentLocation.text = getString(R.string.loading)
        currentLocationComponent.getCurrentLocation()
    }
}