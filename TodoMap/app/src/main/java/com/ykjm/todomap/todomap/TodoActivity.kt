package com.ykjm.todomap.todomap

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.ykjm.todomap.todomap.R
import java.text.SimpleDateFormat
import java.util.*

class TodoActivity : AppCompatActivity(), EmojiPickerDialogFragment.OnEmojiSelectedListener {

    private lateinit var emotionButton: Button
    private lateinit var emotionTextView: TextView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var selectedDate: String
    private lateinit var dateTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar_todo)

        emotionButton = findViewById(R.id.emoButton)
        emotionTextView = findViewById(R.id.emotionTextView)

        sharedPreferences = getSharedPreferences("emoji_prefs", MODE_PRIVATE)

        emotionButton.setOnClickListener {
            showEmojiPickerDialog()
        }

        // 선택된 날짜 받아오기
        selectedDate = intent.getStringExtra("SELECTED_DATE") ?: ""
        if (selectedDate.isNotEmpty()) {
            dateTextView = findViewById(R.id.dateTextView)
            // 선택된 날짜 텍스트뷰에 설정
            dateTextView.text = selectedDate
            // 해당 날짜에 저장된 이모티콘 불러오기
            val savedEmoji = loadEmojiFromSharedPreferences(selectedDate)
            updateSelectedEmoji(savedEmoji)
        } else {
            // 오늘 날짜 텍스트뷰에 설정
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val todayDate = sdf.format(Date())
            dateTextView.text = todayDate
            emotionButton.text = ""
            emotionTextView.text = "오늘의 이모티콘은?"

            // 오늘 날짜에 저장된 이모티콘 불러오기
            val todayEmoji = loadEmojiFromSharedPreferences(todayDate)
            updateSelectedEmoji(todayEmoji)
        }

        findViewById<Button>(R.id.backButton)?.setOnClickListener {
            goBackToCalendar()
        }
    }

    private fun updateSelectedEmoji(selectedEmoji: String) {
        emotionButton.text = selectedEmoji
        emotionTextView.text = "오늘의 이모티콘: $selectedEmoji"
    }

    private fun saveEmojiToSharedPreferences(selectedDate: String, emoji: String) {
        val editor = sharedPreferences.edit()
        editor.putString(selectedDate, emoji)
        editor.apply()
    }

    private fun loadEmojiFromSharedPreferences(selectedDate: String): String {
        return sharedPreferences.getString(selectedDate, "") ?: ""
    }

    private fun showEmojiPickerDialog() {
        val dialog = EmojiPickerDialogFragment()
        dialog.setOnEmojiSelectedListener(this)
        dialog.show(supportFragmentManager, "EmojiPickerDialog")
    }

    private fun goBackToCalendar() {
        val selectedEmoji = emotionButton.text.toString()
        val intent = Intent().apply {
            putExtra("SELECTED_EMOJI", selectedEmoji)
            putExtra("SELECTED_DATE", selectedDate)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun onEmojiSelected(emoji: String) {
        updateSelectedEmoji(emoji)

        // 선택한 이모지를 SharedPreferences에 저장
        if (selectedDate.isNotEmpty()) {
            saveEmojiToSharedPreferences(selectedDate, emoji)
        } else {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val todayDate = sdf.format(Date())
            saveEmojiToSharedPreferences(todayDate, emoji)
        }
    }
}