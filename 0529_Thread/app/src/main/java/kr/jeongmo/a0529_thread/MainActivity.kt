package kr.jeongmo.a0529_thread

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity(), View.OnClickListener {
    var isRunning = false // 실행 여부 확인용 변수
    var timer: Timer? = null
    var time = 0
    
    private lateinit var btnStart:Button
    private lateinit var btnRefresh: Button
    private lateinit var tvMinute: TextView
    private lateinit var tvSecond: TextView
    private lateinit var tvMillisecond: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 뷰 가져오기
        btnStart = findViewById(R.id.btnStart)
        btnRefresh = findViewById(R.id.btnRefresh)
        tvMinute = findViewById(R.id.tvMinute)
        tvSecond = findViewById(R.id.tvSecond)
        tvMillisecond = findViewById(R.id.tvMillisecond)

        // 버튼별 OnClickListener 등록
        btnStart.setOnClickListener(this)
        btnRefresh.setOnClickListener(this)

    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.btnStart -> {
                if(isRunning) {
                    pause()
                } else {
                    start()
                }
            }
            R.id.btnRefresh -> {
                refresh()
            }
        }
    }
    
    private fun start() {
        // 스톱워치 측정을 시작하는 로직
        btnStart.text = "일시정지"
        btnStart.setBackgroundColor(getColor(R.color.red))
        isRunning = true // 실행 상태 변경
        
        // 스톱워치 측정을 시작하는 로직
        timer = timer(period = 10) {
            time++ // 10밀리초 단위

            // 시간 계산
            val milliSecond = time % 10
            val second = (time%6000) / 100
            val minute = time / 6000

            runOnUiThread {
                if (isRunning) {
                    tvMillisecond.text = if (milliSecond < 10) ".0${milliSecond}" else ".${milliSecond}" // 밀리초
                    tvSecond.text = if (second<10) ":0${second}" else ":${second}" // 초
                    tvMinute.text = "${minute}" // 분
                }
            }
        }
    }
    private fun pause() {
        // 텍스트 변경
        btnStart.text = "시작"
        btnStart.setBackgroundColor(getColor)
    }
    private fun refresh() {
        // 초기화하는 로직
    }
}


























