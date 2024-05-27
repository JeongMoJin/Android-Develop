package com.example.a0527_daegu_map_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.a0527_daegu_map_project.databinding.ActivityMapsBinding
import com.example.a0527_daegu_map_project.retrofit.RetrofitConnection
import com.example.a0527_daegu_map_project.retrofit.RoadResponse
import com.example.a0527_daegu_map_project.retrofit.RoadService
import com.google.android.gms.maps.model.LatLngBounds
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

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

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        loadRoad()
    }
    private fun loadRoad() {
        // 레트로핏 객체를 이용하면 RoadService 인터페이스 구현체를 가져올 수 있음
        val retrofitAPI = RetrofitConnection.getInstance().create(RoadService::class.java)

        val apiKey = "1wMGYoH1onj8LIYDjyTfyuVPZLQc6F31PLdZjBj6jxjEi5P5suF4F9tGV2d38RvWOUj0tpiv6/OmN0NsBd93gg=="
        val pageNo = 1
        val numOfRows = 1590
        val type = "json"
        val lat = 35.86600354443048
        val lot = 128.5938502217387
        val radius = 50

        retrofitAPI.getRoad(apiKey, pageNo, numOfRows, type, lat, lot, radius)
            .enqueue(object : Callback<RoadResponse> {
                override fun onResponse(
                    call: Call<RoadResponse>,
                    response: Response<RoadResponse>
                ) {
                    showRoad(response.body() as RoadResponse)
                }

                override fun onFailure(call: Call<RoadResponse>, t: Throwable) {
                    Toast.makeText(baseContext, "서버에서 데이터를 가져올 수 없습니다",
                        Toast.LENGTH_SHORT).show()
                }

            })
    }
    private fun showRoad(roadResponse: RoadResponse) {
        val latLngBounds = LatLngBounds.builder()

        for (road in roadResponse.body.items.item) {
            val position = LatLng(road.lat.toDouble(), road.lot.toDouble())
            val marker = MarkerOptions().position(position).title(road.parkNm)
            mMap.addMarker(marker)

            latLngBounds.include(marker.position)
        }
        val bounds = latLngBounds.build()
        val padding = 0
        val updated = CameraUpdateFactory.newLatLngBounds(bounds, padding)

        mMap.moveCamera(updated)
    }
}