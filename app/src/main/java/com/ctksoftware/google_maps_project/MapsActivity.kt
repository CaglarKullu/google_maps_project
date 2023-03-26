package com.ctksoftware.google_maps_project

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ctksoftware.google_maps_project.databinding.ActivityMapsBinding
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var currentLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val permissionCode=101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)



        getCurrentUserLocation()
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentUserLocation() {
        if (ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED &&ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)!=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION,),permissionCode)
            return
        }

        val getLocation = fusedLocationClient.lastLocation.addOnSuccessListener {
            location->
            if (location!=null){
                currentLocation=location
                Toast.makeText(this, currentLocation.latitude.toString()+" "+currentLocation.altitude.toString(), Toast.LENGTH_LONG).show()

                // Obtain the SupportMapFragment and get notified when the map is ready to be used.
                val mapFragment = supportFragmentManager
                    .findFragmentById(R.id.map) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

   val home = LatLng(currentLocation.latitude, currentLocation.longitude)

        val markerOptions = MarkerOptions().position(home).title("Home Sweet Home")
        googleMap?.animateCamera(CameraUpdateFactory.newLatLng(home))
        googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(home,15f))
        googleMap?.addMarker(markerOptions)

        //mMap.addMarker(MarkerOptions().position(homeSp).title("Home sweet home"))
       // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(homeSp, 15f))


    }



    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            permissionCode-> if (grantResults.isNotEmpty()&& grantResults [0]==PackageManager.PERMISSION_GRANTED)
                getCurrentUserLocation()
        }
    }

}


