package kr.ykjm.todomap

import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade
import java.time.LocalDate

class EmojiDecorator(private val date: LocalDate, private val emoji: String) : DayViewDecorator {
    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return day?.date?.equals(date) ?: (date == null)
    }

    override fun decorate(view: DayViewFacade?) {
        view?.addSpan(EmojiSpan(emoji))
    }
}


