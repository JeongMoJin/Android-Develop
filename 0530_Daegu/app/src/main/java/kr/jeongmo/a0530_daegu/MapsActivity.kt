package kr.jeongmo.a0530_daegu

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kr.jeongmo.a0530_daegu.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private val defaultLocation = LatLng(35.8714354, 128.601445)
    private val defaultZoomLevel = 15f

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val permissionsRequired = arrayOf(
        android.Manifest.permission.ACCESS_FINE_LOCATION,
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val permissionsRequestCode = 1

    private fun hasPermissions() : Boolean {
        /* 앱에서 사용하는 권한이 있는지 체크 */
        for (permission in permissionsRequired) {
            if (ActivityCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.btnMyLocation.setOnClickListener {
            initMap()
        }
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
        mMap = googleMap
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, defaultZoomLevel))
        if (hasPermissions()) {
            initMap()
        } else {
            ActivityCompat.requestPermissions(this, permissionsRequired, permissionsRequestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        /* 권한 요청 콜백 함수 */
        if (requestCode == permissionsRequestCode) {
            if (PackageManager.PERMISSION_GRANTED == grantResults.firstOrNull()) {
                Toast.makeText(applicationContext, "권한 요청이 승인되었습니다.", Toast.LENGTH_SHORT).show()
                initMap()
            } else {
                Toast.makeText(applicationContext, "권한 요청이 거부되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getMyLocation(): LatLng {
        /* 현재 위치를 반환 */
        val location: Location

        // 위치 서비스 객체를 불러옴.
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // GPS 센서와 네트워크로 마지막으로 업데이트된 위치를 불러옴.
        val locationNetwork : Location?
        = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        val locationGPS: Location?
        = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)

        // 두 개 위치 중 정확도 높은 것으로 선택.
        if (locationGPS != null && locationNetwork != null) {
            location = if (locationGPS.accuracy > locationNetwork.accuracy) {
                locationGPS
            } else {
                locationNetwork
            }
            return LatLng(location.latitude, location.longitude)
        } else if (locationGPS == null && locationNetwork == null) {
            return  defaultLocation
        }else {
            return if (locationGPS == null) {
                LatLng(locationNetwork!!.latitude, locationNetwork.longitude)
            } else {
                LatLng(locationGPS.latitude, locationGPS.longitude)
            }
        }
    }
    private fun initMap() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(getMyLocation(), defaultZoomLevel))
    }
}






























