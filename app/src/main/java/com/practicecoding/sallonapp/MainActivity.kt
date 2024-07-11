package com.practicecoding.sallonapp

import android.os.Bundle
import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.provider.Settings
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.navigation.AppNavigation
import com.practicecoding.sallonapp.ui.theme.SallonAppTheme
import com.practicecoding.sallonapp.ui.theme.purple_200
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            SallonAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = purple_200
                ) {
                    val updatedCurrentUser = FirebaseAuth.getInstance().currentUser
                    var startDestination by remember {
                        mutableStateOf(Screens.Logo.route)
                    }
                    if (updatedCurrentUser != null) {
                        startDestination = Screens.MainScreen.route
                    }
                    AppNavigation(navController = navController, startDestinations = startDestination)
                }
            }
            checkAndEnableLocation()
        }
    }
    private fun checkAndEnableLocation() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            if (!isLocationEnabled()) {
                showEnableLocationDialog()
            } else {
                prepLocationUpdates()
            }
        } else {
            requestSinglePermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    private fun isLocationEnabled(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    private fun showEnableLocationDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Enable Location")
            setMessage("Location access is important to show nearby barbers. Please enable location services.")
            setCancelable(false)
            setPositiveButton("OK") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivityForResult(intent, REQUEST_LOCATION_ENABLE)
            }
            setNegativeButton("Cancel") { _, _ ->
                finish() // Exit the app
            }
            create()
            show()
        }
    }

    private val requestSinglePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                if (!isLocationEnabled()) {
                    showEnableLocationDialog()
                    prepLocationUpdates()
                } else {
                    requestLocationUpdates()
                }
            } else {
                Toast.makeText(this, "GPS Unavailable", Toast.LENGTH_LONG).show()
            }
        }
    private fun requestLocationUpdates() {
    }
    private fun prepLocationUpdates() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            requestLocationUpdates()
        } else {
            requestSinglePermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_LOCATION_ENABLE) {
            if (isLocationEnabled()) {
                requestLocationUpdates()
            } else {
                showEnableLocationDialog()
            }
        }
    }
    companion object {
        private const val REQUEST_LOCATION_ENABLE = 1
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SallonAppTheme {
        Greeting("Android")
    }
}