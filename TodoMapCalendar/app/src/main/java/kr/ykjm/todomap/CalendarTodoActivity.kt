package kr.ykjm.todomap

import kr.ykjm.todomap.CalendarActivity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog

class CalendarTodoActivity : AppCompatActivity() {
    private lateinit var emoButton: Button
    private lateinit var selectedDate: String
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar_todo)

        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            saveChanges()
            finish()
        }

        emoButton = findViewById(R.id.emoButton)
        sharedPreferences = getSharedPreferences("Emojis", MODE_PRIVATE)
        selectedDate = intent.getStringExtra("selectedDate") ?: ""

        emoButton.setOnClickListener {
            showEmojiDialog()
        }
    }

    private fun showEmojiDialog() {
        val emojis = arrayOf("😊", "😢", "😠", "😎")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select Emoji")
        builder.setItems(emojis) { dialog, which ->
            val emoji = emojis[which]
            emoButton.text = emoji
            saveEmoji(selectedDate, emoji)
        }

        val dialog = builder.create()
        dialog.show()
    }

    private fun saveEmoji(date: String, emoji: String) {
        val editor = sharedPreferences.edit()
        editor.putString(date, emoji)
        editor.apply()
    }

    fun goBackToCalendar(view: View) {
        // Save any changes made in CalendarTodoActivity
        saveChanges()

        // Navigate back to CalendarActivity
        val intent = Intent(this, CalendarActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun saveChanges() {
        val selectedDate = findViewById<TextView>(R.id.dateTextView).text.toString()
        val selectedEmoji = findViewById<TextView>(R.id.emotionTextView).text.toString()

        val sharedPreferences = getSharedPreferences("emoji_preferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(selectedDate, selectedEmoji)
        editor.apply()

        val intent = Intent(this, CalendarActivity::class.java)
        intent.putExtra("selectedDate", selectedDate)
        intent.putExtra("selectedEmoji", selectedEmoji)
        startActivity(intent)
    }
}
