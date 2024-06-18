package com.ykjm.todomap.todomap

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.MaterialCalendarView
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener
import java.util.*

class CalendarFragment : Fragment() {

    private lateinit var calendarView: MaterialCalendarView
    private var selectedDate: Long = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.calendar, container, false)
        calendarView = view.findViewById(R.id.mcCalendarView)

        // 날짜 클릭 리스너 설정
        calendarView.setOnDateChangedListener(OnDateSelectedListener { widget, date, selected ->
            selectedDate = date.date.time
            navigateToCalendarTodoActivity()
        })

        return view
    }

    private fun navigateToCalendarTodoActivity() {
        val intent = Intent(requireContext(), CalendarTodoActivity::class.java).apply {
            putExtra("SELECTED_DATE", selectedDate)
        }
        startActivity(intent)
    }
}
