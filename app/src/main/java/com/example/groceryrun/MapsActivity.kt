package com.example.groceryrun

import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.groceryrun.Common.Common
import com.example.groceryrun.Model.MyPlaces
import com.example.groceryrun.Remote.IGoogleAPIService
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_maps.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {
    override fun onMarkerClick(p0: Marker?) = false

    private lateinit var map: GoogleMap

    private var latitude:Double=0.toDouble()
    private var longitude:Double=0.toDouble()

    private lateinit var lastLocation: Location
    private var mMarker: Marker?=null

    //Location
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

    private fun setUpMap() {
        Log.i("Couldn't load json", "Couldn't load json file")
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        map.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                latitude = location.latitude
                longitude=location.longitude
                val currentLatLng = LatLng(location.latitude, location.longitude)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 12f))
            }
        }

    }

    lateinit var mService:IGoogleAPIService
    internal lateinit var currentPlaces: MyPlaces

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Init Service
        mService = Common.googleApiService
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        bottom_navigation_view.setOnNavigationItemSelectedListener { item->
            when (item.itemId){
                R.id.action_market -> nearByPlace ("Grocery Store")
                R.id.action_foodbank -> nearByPlace ("Foodbanks")
            }
            true
        }
    }

    private fun nearByPlace(typePlace: String) {
        //Clear all the marker on the map
        map.clear()
        //Build URL request based on location
        var url = getUrl(latitude, longitude,typePlace)

        mService.getNearbyPlaces(url)
            .enqueue(object:Callback<MyPlaces>{
                override fun onResponse(call: Call<MyPlaces>, response: Response<MyPlaces>) {
                    currentPlaces = response!!.body()!!

                    if (response!!.isSuccessful)
                    {
                        for (i in 0 until response!!.body()!!.results!!.size) {
                            val markerOptions = MarkerOptions()
                            val googlePlace = response.body()!!.results!![i]
                            val lat = googlePlace.geometry!!.location!!.lat
                            val lng = googlePlace.geometry!!.location!!.lng
                            val placeName = googlePlace.name
                            val latLng = LatLng(lat, lng)

                            markerOptions.position(latLng)
                            markerOptions.title(placeName)
                            if (typePlace.equals("supermarket") || typePlace.equals("store") || typePlace.equals("convenience_store") || typePlace.equals("department_store"))
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_grocery_store))
                            else if (typePlace.equals("Food Banks"))
                                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_food_bank))
                            else
                                markerOptions.icon(
                                    BitmapDescriptorFactory.defaultMarker(
                                        BitmapDescriptorFactory.HUE_BLUE
                                    )
                                )

                            markerOptions.snippet(i.toString()) //Assign index for market
                            //Add marker to map
                            map!!.addMarker(markerOptions)
                            //Move Camera
                            map!!.moveCamera(CameraUpdateFactory.newLatLng(latLng))
                            map!!.animateCamera(CameraUpdateFactory.zoomTo(15f))
                        }
                    }

                }
                override fun onFailure(call: Call<MyPlaces>, t: Throwable) {
                    Toast.makeText(baseContext,""+t!!.message,Toast.LENGTH_LONG).show()
                }
            })
    }

    private fun getUrl(latitude: Double, longitude: Double, typePlace: String): String {
        Log.i("Couldn't load json", "hi?")
        val googlePlaceUrl =
            StringBuilder("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
        googlePlaceUrl.append("?location=$latitude,$longitude")
        googlePlaceUrl.append("radius=10000") //10km radius
        googlePlaceUrl.append("&type=$typePlace")
        googlePlaceUrl.append("&key=AIzaSyCb1NTO_9C7KVvqnhhHxp5Gi7kwja6949I")
        Log.i("Couldn't load json", "google API?")
        Log.d ("URL_DEBUG", googlePlaceUrl.toString())
        return googlePlaceUrl.toString()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.getUiSettings().setZoomControlsEnabled(true)
        map.setOnMarkerClickListener(this)
        Log.i("Couldn't load json", "Couldn't load json file")
        setUpMap()
    }
}
