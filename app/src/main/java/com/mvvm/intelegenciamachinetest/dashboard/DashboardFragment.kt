package com.mvvm.intelegenciamachinetest.dashboard

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.gms.location.*
import com.mvvm.intelegenciamachinetest.IntelegenciaApplication
import com.mvvm.intelegenciamachinetest.databinding.FragmentDashboardBinding
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class DashboardFragment : Fragment() {

    private lateinit var viewDataBinding: FragmentDashboardBinding
    private val viewModel by viewModels<DashBoardViewModel> {
        val application = (requireContext().applicationContext as IntelegenciaApplication)
        DashViewModelFactory(application, application.taskRepository)
    }

    // Location
    private val PERMISSION_LOCATION_ID = 45
    lateinit var mFusedLocationClient: FusedLocationProviderClient


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        viewDataBinding = FragmentDashboardBinding.inflate(inflater, container, false).apply {
            viewmodel = viewModel
        }
        return viewDataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewDataBinding.lifecycleOwner = this.viewLifecycleOwner

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        startAlarmManager()
        getLastLocation()
    }


    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {

                mFusedLocationClient.lastLocation.addOnCompleteListener(this.requireActivity()) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        val lat = location.latitude.toString()
                        val lon = location.longitude.toString()
                        saveLatLon(lat, lon)
                        viewModel.getWeatherReport(lat, lon)
                    }
                }
            } else {
                Toast.makeText(this.requireContext(), "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this.requireActivity())
        mFusedLocationClient!!.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation

            val lat = mLastLocation.latitude.toString()
            val lon = mLastLocation.longitude.toString()
            saveLatLon(lat, lon)
            viewModel.getWeatherReport(lat, lon)
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = activity?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        requestPermissions(
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_LOCATION_ID
        )
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_LOCATION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }



    fun startAlarmManager(){
        val mIntent = Intent(this.requireContext(), AlarmReceiver::class.java)
        val mPendingIntent = PendingIntent.getBroadcast(this.requireContext(), 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val mAlarmManager: AlarmManager = this.activity?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        mAlarmManager!!.setRepeating(
            AlarmManager.RTC_WAKEUP, System.currentTimeMillis(),
            120000, mPendingIntent
        )
    }

    fun saveLatLon(lat: String, lon: String){
        val sharedPreference =  activity?.getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
        var editor = sharedPreference?.edit()
        editor?.putString("lat",lat)
        editor?.putString("lon",lon)
        editor?.commit()
    }

    fun getWeatherData(){
        val sharedPreference =  activity?.getSharedPreferences("PREFERENCE_NAME",Context.MODE_PRIVATE)
        sharedPreference?.getString("lat", "")
        sharedPreference?.getString("lon", "")

        val lat = sharedPreference?.getString("lat", "")
        val lon = sharedPreference?.getString("lon", "")

        if (!lat.equals("") && !lon.equals("")) {
            viewModel.getWeatherReportapi(lat!!, lon!!)
        }
    }

    @Subscribe
    fun callBackListener(str : String) {

        val connManager = activity?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val mWifi = connManager!!.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
        if (mWifi.isConnected) {
            getWeatherData()
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this);
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this);
    }
}
