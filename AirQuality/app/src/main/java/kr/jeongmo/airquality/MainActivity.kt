package kr.jeongmo.airquality

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kr.jeongmo.airquality.databinding.ActivityMainBinding
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    // 런타임 권한 요청 시 필요한 요청 코드
    private val PERMISSIONS_REQUEST_CODE = 100

    // 요청할 권한 목록
    var REQUIRED_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    // 위치 서비스 요청 시 필요한 런처
    lateinit var getGPSPermissionLauncher: ActivityResultLauncher<Intent>

    // 위도와 경도를 가져올 때 필요합니다.
    lateinit var locationProvider: LocationProvider

    fun getCurrentAddress(latitude:Double, longitude:Double) : Address? {
        val geocoder = Geocoder(this, Locale.getDefault())
        // Address 객체는 주소와 관련된 여러 정보를 가지고 있습니다.
        // android.location.Address 패키지참고.
        val addresses: List<Address>?

        addresses = try {
            // GeoCoder 객체를 이용하여 위도와 경도로부터 리스트를 가져옵니다.
            geocoder.getFromLocation(latitude, longitude, 7)
        } catch (ioException: IOException) {
            Toast.makeText(this, "지오코더 서비스 사용불가입니다.", Toast.LENGTH_SHORT).show()
            return null
        } catch (illegaArgumentException: IllegalArgumentException) {
            Toast.makeText(this, "잘못된 위도, 경도 입니다.", Toast.LENGTH_SHORT).show()
            return null
        }
        // 에러는 아니지만 주소가 발견되지 않은 경우
        if (addresses == null || addresses.size == 0) {
            Toast.makeText(this, "주소가 발견되지 않았습니다.", Toast.LENGTH_SHORT).show()
            return null
        }
        val address: Address = addresses[0]
        Log.d("주소", address.toString())
        return address
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAllPermissions() // 권한 확인
        updateUI()
    }

    private fun updateUI() {
        locationProvider = LocationProvider(this@MainActivity)

        // 위도와 경도 정보를 가져옵니다.
        val latitude: Double = locationProvider.getLocationLatitude()
        val longitude : Double = locationProvider.getLocationLongtitude()

        if (latitude != 0.0 || longitude != 0.0) {
            // 1. 현재 위치를 가져오고 UI 업데이트
            // 현재 위치를 가져오기
            val address = getCurrentAddress(latitude, longitude)
            // 주소가 null이 아닐 경우 UI 업데이트
            address?.let {
                binding.tvLocationTitle.text = "${it.thoroughfare}" // 예시 : 역삼 1동
                binding.tvLocationSubtitle.text = "${it.countryName}" +
                        "${it.adminArea}" // 예 : 대한민국 서울특별시
            }

            // 2. 현재 미세먼지 농도를 가져오고 UI 업데이트
        } else {
            Toast.makeText(this@MainActivity, "위도, 경도 정보를 가져올 수 없습니다. 새로고침을 눌러주세요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkAllPermissions() {
        // 1. 위치 서비스(GPS)가 켜져 있는지 확인
        if (!isLocationServicesAvailable()) {
            showDialogForLocationServiceSetting();
        } else { // 2. 런타입 앱 권한이 모두 허용되어 있는지 확인
            isRunTimePermissionsGranted();
        }
    }

    fun isLocationServicesAvailable(): Boolean {
        val locationManager = getSystemService(LOCATION_SERVICE) as
                LocationManager

        return (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
    }
    fun isRunTimePermissionsGranted() {
        // 위치 퍼미션을 가지고 있는지 체크
        val hasFineLocationPermission = ContextCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val hasCoarseLocationPermission = ContextCompat.checkSelfPermission(
            this@MainActivity,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        if (hasFineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED) {
            // 권한이 한 개라도 없다면 퍼미션 요청
            ActivityCompat.requestPermissions(this@MainActivity,
            REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST_CODE && grantResults.size ==
                REQUIRED_PERMISSIONS.size) {

            // 요청 코드가 PERMISSIONS_REQUEST_CODE이고, 요청한 퍼미션 개수만큼
            // 수신되었다며
            var checkResult = true

            // 모든 퍼미션을 허용했는지 체크
            for (result in grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    checkResult = false
                    break
                }
            }
            if (checkResult) {
                // 위칫값을 가져올 수 있음
                updateUI()
            } else {
                // 퍼미션을 거부되었다면 앱 종료
                Toast.makeText(this@MainActivity, "퍼미션이 거부되었습니다. 앱을 다시 실행하여" +
                        "퍼미션을 허용해주세요", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
    private fun showDialogForLocationServiceSetting() {
        // 먼저 ActivityResultLauncher를 설정해줍니다. 이 런처를 이용하여 결괏값을
        // 반환해야 하는 인텐트를 실행할 수 있습니다.
        getGPSPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {result ->
            // 결괏값을 받았을 때 로직
            if (result.resultCode == Activity.RESULT_OK) {
                // 사용자가 GPS를 활성화 시켰는지 확인
                if (isLocationServicesAvailable()) {
                    isRunTimePermissionsGranted() // 런타임 권한 확인
                } else {
                    // 위치 서비스가 허용되지 않았다면 앱 종료
                    Toast.makeText(this@MainActivity, "위치 서비스를 사용할 수 없습니다",
                        Toast.LENGTH_SHORT).show()
                    finish() // 액티비티 종료
                }
            }
        }
        val builder : AlertDialog.Builder = AlertDialog.Builder(
            this@MainActivity
        ) // 사용자에게 의사를 물어보는 AlertDialog 생성
        builder.setTitle("위치 서비스 비활성화") // 제목 설정
        builder.setMessage("위치 서비스가 꺼져 있습니다. 설정해야 앱을 사용할 수 있습니다.") // 내용 설정
        builder.setCancelable(true) // 다이얼로그 창 바깥 터치 시 창 닫힘
        builder.setPositiveButton("설정",
            DialogInterface.OnClickListener {
            dialog, id -> // 확인 버튼 설정
            val callGPSSettingIntent = Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS)
            getGPSPermissionLauncher.launch(callGPSSettingIntent)
        })
        builder.setNegativeButton("취소", // 취소버튼 설정
            DialogInterface.OnClickListener {
                dialog, id ->
                dialog.cancel()
                Toast.makeText(this@MainActivity, "기기에서 위치서비스(GPS) 설정 후 사용해주세요.",
                    Toast.LENGTH_SHORT).show()
                finish()
            })
        builder.create().show() // 다이얼로그 생성 및 보여주기
    }


}




















