package com.example.finedustalarm

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.finedustalarm.databinding.ActivityStationBinding
import net.daum.android.map.MapActivity
import net.daum.mf.map.api.MapPoint

class stationActivity : AppCompatActivity() {
    lateinit var binding: ActivityStationBinding
    val PERMISSIONS_REQUEST_CODE = 100
    var REQUIRED_PERMISSIONS = arrayOf<String>( Manifest.permission.ACCESS_FINE_LOCATION)
    private lateinit var mPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        mPreferences = getSharedPreferences(MainActivity.Music, MODE_PRIVATE);
        val preferencesEditor: SharedPreferences.Editor = mPreferences.edit()

        binding = ActivityStationBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.stationTxt1.text = mPreferences.getString("addr0", null)
        binding.stationTxt2.text = mPreferences.getString("addr1", null)
        binding.stationTxt3.text = mPreferences.getString("addr2", null)

        val permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        if(permissionCheck == PackageManager.PERMISSION_GRANTED) {
            val lm: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            try {
                val userNowLocation: Location? =
                    lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                val uLatitude = userNowLocation?.latitude
                val uLongitude = userNowLocation?.longitude
                val uNowPosition = MapPoint.mapPointWithGeoCoord(uLatitude!!, uLongitude!!)
                binding.mapView2.setMapCenterPoint(uNowPosition, true)
            }catch(e: NullPointerException){
                Log.e("LOCATION_ERROR", e.toString())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    ActivityCompat.finishAffinity(this)
                }else{
                    ActivityCompat.finishAffinity(this)
                }

                val intent = Intent(this, stationActivity::class.java)
                startActivity(intent)
                System.exit(0)
            }
        }else{
            Toast.makeText(this, "위치 권한이 없습니다.", Toast.LENGTH_SHORT).show()
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE )
        }
    }
}