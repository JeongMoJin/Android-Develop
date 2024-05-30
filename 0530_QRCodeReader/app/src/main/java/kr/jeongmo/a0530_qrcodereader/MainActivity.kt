package kr.jeongmo.a0530_qrcodereader

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.camera.core.Preview
import kr.jeongmo.a0530_qrcodereader.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
    fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

    }
    fun getPreview() :Preview {
        val preview : Preview = Preview.Builder().build()
        preview.setSurfaceProvider(binding.barcodePreview.getSurfaceProvider())
        return preview
    }
}