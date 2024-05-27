package com.example.myapplication

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.myapplication.databinding.ActivityMapsBinding

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.clustering.ClusterManager
import kr.jeongmo.a0524__map_project.retrofit.LibraryResponse
import kr.jeongmo.a0524__map_project.retrofit.LibraryService
import kr.jeongmo.a0524__map_project.retrofit.RetrofitConnection
import kr.jeongmo.a0524__map_project.retrofit.Row
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var clusterManager: ClusterManager<Row>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
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

        // 클러스터 매니저 세팅
        clusterManager = ClusterManager(this, mMap)
        mMap.setOnCameraIdleListener(clusterManager) // 화면을 이동 후 멈췄을 때 설정
        mMap.setOnMarkerClickListener(clusterManager) // 마커 클릭 설정

        loadLibrary()

//        mMap.setOnMarkerClickListener {
//            if (it.tag != null) {
//                var url = it.tag as String
//                if (!url.startsWith("http")) {
//                    url = "http://${url}"
//                }
//                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
//                startActivity(intent)
//            }
//            true
//        }
    }

    private fun loadLibrary() {
        // 레트로핏 객체를 이용하면 LibraryService 인터페이스 구현체를 가져올 수 있음
        val retrofitAPI = RetrofitConnection.getInstance().create(LibraryService::class.java)
        retrofitAPI.getLibrary("4b486f51526477633737584b4c4843").enqueue(object :
            Callback<LibraryResponse> {
            override fun onResponse(
                call: Call<LibraryResponse>,
                response: Response<LibraryResponse>
            ) {
                showLibrary(response.body() as LibraryResponse)
            }
            override fun onFailure(call: Call<LibraryResponse>, t: Throwable) {
                Toast.makeText(baseContext, "서버에서 데이터를 가져올 수 없습니다.", Toast.LENGTH_SHORT).show()
            }
        })
    }
    private fun showLibrary(libraryResponse: LibraryResponse) {
        val latLngBounds = LatLngBounds.builder()

        for (lib in libraryResponse.SeoulPublicLibraryInfo.row) {
            // 기존 마커 세팅코드는 삭제하고 클러스터 매니저에 데이터를 추가하는 코드만 넣어줌.
            clusterManager.addItem(lib)
            val position = LatLng(lib.XCNTS.toDouble(), lib.YDNTS.toDouble())
//            val marker = MarkerOptions().position(position).title(lib.LBRRY_NAME)
////            mMap.addMarker(marker)
//            val obj = mMap.addMarker(marker)
//            obj?.tag = lib.HMPG_URL

            latLngBounds.include(position)
        }
        val bounds = latLngBounds.build()
        val padding = 0
        val updated = CameraUpdateFactory.newLatLngBounds(bounds, padding)

        mMap.moveCamera(updated)
    }
}