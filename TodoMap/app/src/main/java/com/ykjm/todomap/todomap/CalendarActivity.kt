package com.ykjm.todomap.todomap

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.ykjm.todomap.todomap.R
import java.text.SimpleDateFormat
import java.util.*

class CalendarActivity : AppCompatActivity(), EmojiPickerDialogFragment.OnEmojiSelectedListener {

    private lateinit var calendarView: MaterialCalendarView
    private lateinit var sharedPreferences: SharedPreferences
    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar)

        calendarView = findViewById(R.id.mcCalendarView)
        sharedPreferences = getSharedPreferences("emoji_prefs", MODE_PRIVATE)

        // EmojiDecorator를 생성하고 캘린더에 데코레이터 추가
        val emojiDecorator = EmojiDecorator(getDatesWithEmoji(), calendarView, resources, sharedPreferences)
        emojiDecorator.decorateCalendar()

        calendarView.setOnDateChangedListener(object : OnDateSelectedListener {
            override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
                val selectedDate = sdf.format(date.date)

                // 해당 날짜에 저장된 이모티콘을 불러옴
                val savedEmoji = loadEmojiFromSharedPreferences(selectedDate)
                updateSelectedEmojiOnCalendar(savedEmoji)

                // 두 번째 페이지로 이동
                navigateToSecondPage(selectedDate)
            }
        })


        // 초기화면으로 오늘 날짜에 저장된 이모티콘 표시
        val today = CalendarDay.today().date
        val todayDate = sdf.format(today)
        val savedEmoji = loadEmojiFromSharedPreferences(todayDate)
        updateSelectedEmojiOnCalendar(savedEmoji)
    }

    private fun updateSelectedEmojiOnCalendar(selectedEmoji: String) {
        findViewById<TextView>(R.id.dateTextView)?.text = selectedEmoji
    }

    private fun loadEmojiFromSharedPreferences(selectedDate: String): String {
        return sharedPreferences.getString(selectedDate, "") ?: ""
    }

    private fun navigateToSecondPage(selectedDate: String) {
        val intent = Intent(this, TodoActivity::class.java)
        intent.putExtra("SELECTED_DATE", selectedDate)
        startActivityForResult(intent, REQUEST_EMOJI_SELECTION)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_EMOJI_SELECTION && resultCode == RESULT_OK) {
            val selectedEmoji = data?.getStringExtra("SELECTED_EMOJI")
            val selectedDate = data?.getStringExtra("SELECTED_DATE")

            if (!selectedDate.isNullOrEmpty() && !selectedEmoji.isNullOrEmpty()) {
                saveEmojiToSharedPreferences(selectedDate, selectedEmoji)
                calendarView.selectedDate?.let {
                    updateSelectedEmojiOnCalendar(selectedEmoji)
                }
            }
        }
    }

    override fun onEmojiSelected(emoji: String) {
        // 이모티콘 선택 이벤트 처리 (두 번째 페이지에서 처리)
    }

    private fun saveEmojiToSharedPreferences(selectedDate: String, emoji: String) {
        val editor = sharedPreferences.edit()
        editor.putString(selectedDate, emoji)
        editor.apply()
    }

    companion object {
        const val REQUEST_EMOJI_SELECTION = 101
    }

    private fun getDatesWithEmoji(): List<String> {
        val datesWithEmoji = mutableListOf<String>()
        val keys = sharedPreferences.all.keys

        // 모든 SharedPreferences 키를 순회하며 이모지가 있는 날짜를 찾음
        for (key in keys) {
            if (key.matches(Regex("\\d{4}-\\d{2}-\\d{2}"))) { // 날짜 형식 확인 (예: 2024-06-14)
                val emoji = loadEmojiFromSharedPreferences(key)
                if (emoji.isNotEmpty()) {
                    datesWithEmoji.add(key)
                }
            }
        }

        return datesWithEmoji
    }
}