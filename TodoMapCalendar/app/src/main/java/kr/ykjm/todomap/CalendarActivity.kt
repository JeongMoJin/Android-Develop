package kr.ykjm.todomap


import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import kr.ykjm.todomap.CalendarTodoActivity
import kr.ykjm.todomap.EmojiDecorator
import kr.ykjm.todomap.R
import java.time.LocalDate



class CalendarActivity : AppCompatActivity() {
    private lateinit var calendarView: MaterialCalendarView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar)

        calendarView = findViewById(R.id.mcCalendarView)

        calendarView.setOnDateChangedListener { _, date, _ ->
            val intent = Intent(this@CalendarActivity, CalendarTodoActivity::class.java)
            intent.putExtra("selectedDate", date.date.toString())
            startActivityForResult(intent, REQUEST_TODO)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_TODO && resultCode == Activity.RESULT_OK) {
            val selectedDate = data?.getStringExtra("selectedDate")
            val selectedEmoji = data?.getStringExtra("selectedEmoji")
            selectedDate?.let { date ->
                selectedEmoji?.let { emoji ->
                    val sharedPreferences = getSharedPreferences("emoji_preferences", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString(date, emoji)
                    editor.apply()
                }
            }
            updateCalendarDecorators()
        }
    }

    private fun updateCalendarDecorators() {
        calendarView.removeDecorators()
        val sharedPreferences = getSharedPreferences("emoji_preferences", Context.MODE_PRIVATE)
        val keys = sharedPreferences.all.keys
        for (key in keys) {
            val emoji = sharedPreferences.getString(key, "")
            emoji?.let {
                val date = LocalDate.parse(key)
                val decorator = EmojiDecorator(date, it)
                calendarView.addDecorator(decorator)
            }
        }
    }

    companion object {
        private const val REQUEST_TODO = 100
    }
}
