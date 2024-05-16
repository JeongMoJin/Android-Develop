package kr.jeongmo.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import java.util.*
import kotlin.concurrent.timer

class MainActivity : AppCompatActivity(), View.OnClickListener {

    var isRunning = false
    var timer: Timer? = null // timer 변수 추가
    var time = 0 // time 변수 추가

    private lateinit var btn_start:Button
    private lateinit var btn_refresh:Button
    private lateinit var tv_millisecond: TextView
    private lateinit var tv_second: TextView
    private lateinit var tv_minute: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_start = findViewById(R.id.btn_start)
        btn_refresh = findViewById(R.id.btn_refresh)
        tv_millisecond = findViewById(R.id.tv_millisecond)
        tv_second = findViewById(R.id.tv_second)
        tv_minute = findViewById(R.id.tv_minute)

        btn_start.setOnClickListener(this)
        btn_refresh.setOnClickListener(this)
    }

    override fun onClick(v:View?) {
        when(v?.id) {
            R.id.btn_start -> {
                if(isRunning) {
                    pause()
                } else {
                    start()
                }
            }
            R.id.btn_refresh -> {
                refresh()
            }
        }
    }

    private fun start() {
        btn_start.text = "일시정지"
        btn_start.setBackgroundColor(getColor(R.color.red))
        isRunning = true

        // 스톱워치를 시작하는 로직
        timer = timer(period = 10) {
            time++ // 10밀리초 단위 타이머

            // 시간 계산
            val milli_second = time % 100
            val second = (time%6000) / 100
            val minute = time / 6000

            runOnUiThread {
                if (isRunning) {
                    // 밀리초
                    tv_millisecond.text =
                        if (milli_second < 10) ".0${milli_second}" else ".${milli_second}"
                    // 초
                    tv_second.text = if (second<10) ":0${second}" else ":${second}"
                    // 분
                    tv_minute.text = "${minute}"
                }
            };
        }
    }
    private fun pause() {
        // 텍스트 속성 변경
        btn_start.text = "시작"
        btn_start.setBackgroundColor(getColor(R.color.blue))

        isRunning = false // 멈춤 상태로 전환
        timer?.cancel() // 타이머 멈추기

    }
    private fun refresh() {
        timer?.cancel() // 백그라운드 타이머 멈추기

        btn_start.text = "시작"
        btn_start.setBackgroundColor(getColor(R.color.blue))
        isRunning = false

        time = 0
        tv_millisecond.text = ".00"
        tv_second.text = ":00"
        tv_minute.text = "00"

    }
}