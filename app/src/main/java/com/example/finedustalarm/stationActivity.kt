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
import net.daum.mf.map.api.MapPOIItem
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
        binding.stationTxt5.text = "업데이트 일시: " + mPreferences.getString("dataTime", null)
        val point = MapPOIItem()
        point.apply {
            itemName = "풍덕천1동주민자치센터"
            mapPoint = MapPoint.mapPointWithGeoCoord(37.319719,
                127.088704)
            markerType = MapPOIItem.MarkerType.BluePin
            selectedMarkerType = MapPOIItem.MarkerType.RedPin
            isCustomImageAutoscale = false
        }
        binding.mapView2.addPOIItem(point)

        val point2 = MapPOIItem()
        point2.apply {
            itemName = "기흥구청"
            mapPoint = MapPoint.mapPointWithGeoCoord(37.28058,
                127.11470)
            markerType = MapPOIItem.MarkerType.BluePin
            selectedMarkerType = MapPOIItem.MarkerType.RedPin
            isCustomImageAutoscale = false
        }
        binding.mapView2.addPOIItem(point2)

        val point3 = MapPOIItem()
        point3.apply {
            itemName = "행정복지센터"
            mapPoint = MapPoint.mapPointWithGeoCoord(37.36089,
                127.11941)
            markerType = MapPOIItem.MarkerType.BluePin
            selectedMarkerType = MapPOIItem.MarkerType.RedPin
            isCustomImageAutoscale = false
        }
        binding.mapView2.addPOIItem(point3)

        val point4 = MapPOIItem()
        point4.apply {
            var x1 = mPreferences.getString("latitude", null)!!.toDouble()
            var y1 = mPreferences.getString("longitude", null)!!.toDouble()
            itemName = "현재 위치"
            mapPoint = MapPoint.mapPointWithGeoCoord(x1,
                y1)
            markerType = MapPOIItem.MarkerType.BluePin
            selectedMarkerType = MapPOIItem.MarkerType.RedPin
            isCustomImageAutoscale = false
        }
        binding.mapView2.addPOIItem(point4)

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
                binding.mapView2.setMapCenterPointAndZoomLevel(uNowPosition,7,true)
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