package com.ykjm.todomap.todomap

import android.content.SharedPreferences
import android.content.res.Resources
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.spans.DotSpan
import com.ykjm.todomap.todomap.R
import java.text.SimpleDateFormat
import java.util.*

class EmojiDecorator(
    private val datesWithEmoji: List<String>,
    private val calendarView: MaterialCalendarView,
    private val resources: Resources,
    private val sharedPreferences: SharedPreferences
) {

    private val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

    fun decorateCalendar() {
        for (dateString in datesWithEmoji) {
            val date = sdf.parse(dateString)
            date?.let {
                val calendarDay = CalendarDay.from(it)
                val emoji = loadEmojiFromSharedPreferences(dateString)
                if (emoji.isNotEmpty()) {
                    calendarView.addDecorator(object : DayViewDecorator {
                        override fun shouldDecorate(day: CalendarDay): Boolean {
                            return day == calendarDay
                        }

                        override fun decorate(view: DayViewFacade) {
                            view.addSpan(DotSpan(5f, resources.getColor(R.color.purple_200, null)))
                        }
                    })
                }
            }
        }
    }

    private fun loadEmojiFromSharedPreferences(selectedDate: String): String {
        return sharedPreferences.getString(selectedDate, "") ?: ""
    }
}