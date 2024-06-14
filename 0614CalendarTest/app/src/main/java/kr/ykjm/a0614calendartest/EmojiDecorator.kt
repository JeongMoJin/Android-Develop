package kr.ykjm.a0614calendartest

import android.content.SharedPreferences
import android.content.res.Resources
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.spans.DotSpan
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
        calendarView.removeDecorators() // 기존 데코레이터 제거

        for (date in datesWithEmoji) {
            val calendar = Calendar.getInstance()
            calendar.time = sdf.parse(date) ?: continue
            val calendarDay = CalendarDay.from(calendar)

            calendarView.addDecorator(object : DayViewDecorator {
                override fun shouldDecorate(day: CalendarDay?): Boolean {
                    return day != null && day == calendarDay
                }

                override fun decorate(view: DayViewFacade?) {
                    view?.addSpan(DotSpan(5f, resources.getColor(R.color.teal_200)))
                }
            })
        }
    }

    private fun loadEmojiFromSharedPreferences(selectedDate: String): String {
        return sharedPreferences.getString(selectedDate, "") ?: ""
    }
}
