package com.practicecoding.sallonapp

import android.os.Bundle
import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.ads.MobileAds
import com.google.firebase.auth.FirebaseAuth
import com.practicecoding.sallonapp.appui.Screens
import com.practicecoding.sallonapp.appui.navigation.AppNavigation
import com.practicecoding.sallonapp.appui.viewmodel.PaymentViewModel
import com.practicecoding.sallonapp.ui.theme.SallonAppTheme
import com.practicecoding.sallonapp.ui.theme.purple_200
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity(), PaymentResultWithDataListener {

    private val paymentViewModel: PaymentViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Checkout.preload(applicationContext)
        setContent {

            val navController = rememberNavController()
            SallonAppTheme {

               LaunchedEffect(Unit) {
                   val backgroundScope = CoroutineScope(Dispatchers.IO)
                   backgroundScope.launch {
                       // Initialize the Google Mobile Ads SDK on a background thread.
                       MobileAds.initialize(this@MainActivity) {}
                   }


               }

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = purple_200
                ) {
                    var startDestination by remember {
                        mutableStateOf(Screens.Logo.route)
                    }
                        val updatedCurrentUser = FirebaseAuth.getInstance().currentUser
                        if (updatedCurrentUser != null) {
                            startDestination = Screens.MainScreen.route
                        }
                        AppNavigation(
                            navController = navController,
                            startDestinations = startDestination,
                            paymentViewModel = paymentViewModel
                        )

                }

            }
            checkAndEnableLocation() // Ensure location is enabled on start
        }
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkAndEnableLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
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
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }
    private fun showEnableLocationDialog() {
        AlertDialog.Builder(this).apply {
            setTitle("Enable Location")
            setMessage("Location access is mandatory to show nearby barbers. Please enable location services.")
            setCancelable(false) // Non-cancelable dialog
            setPositiveButton("OK") { _, _ ->
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
            create()
            show()
        }
    }

    private val requestSinglePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                requestLocationUpdates() // If permission is granted, start location updates
                requestSmsPermissionLauncher.launch(Manifest.permission.SEND_SMS)
            } else {
            }
        }

    private val requestSmsPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->

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
    override fun onPaymentSuccess(razorpayPaymentID: String?,paymentData: PaymentData?) {
        if (razorpayPaymentID != null){
            paymentViewModel.handlePaymentSuccess(razorpayPaymentID)
        }
    }
    override fun onPaymentError(code: Int, response: String?, paymentData: PaymentData?) {
        paymentViewModel.handlePaymentError(code,response?:"Payment Failed")
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