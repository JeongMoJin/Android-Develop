package kr.ykjm.a0611_koreait_map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import kr.ykjm.a0611_koreait_map.databinding.ActivityMapsBinding
import kr.ykjm.a0611_koreait_map.retrofit.ParkRespone
import kr.ykjm.a0611_koreait_map.retrofit.ParkService
import kr.ykjm.a0611_koreait_map.retrofit.RetrofitConnection
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
        loadPark()
    }

    private fun loadPark() {
        // 레트로핏 객체 이용해서 인터페이스 구현체 가져옵니다.
        val retrofitAPI = RetrofitConnection.getInstace().create(ParkService::class.java)

        val apiKey = "1wMGYoH1onj8LIYDjyTfyuVPZLQc6F31PLdZjBj6jxjEi5P5suF4F9tGV2d38RvWOUj0tpiv6/OmN0NsBd93gg=="
        val pageNo = 1
        val numOfRows = 200
        val type = "json"
        val lat = 35.86443327590459
        val lot = 128.59334273454948
        val radius = 500

        retrofitAPI.getPark(apiKey, pageNo, numOfRows, type, lat, lot, radius)
            .enqueue(object : Callback<ParkRespone> {
                override fun onResponse(
                    call: Call<ParkRespone>,
                    response: Response<ParkRespone>
                ) {
                    showPark(response.body() as ParkRespone)
                }

                override fun onFailure(call: Call<ParkRespone>, t: Throwable) {
                    Toast.makeText(baseContext, "서버에서 데이터를 가져올 수 없습니다",
                        Toast.LENGTH_SHORT).show()
                }

            })
    }
    private fun showPark(roadResponse: ParkRespone) {
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