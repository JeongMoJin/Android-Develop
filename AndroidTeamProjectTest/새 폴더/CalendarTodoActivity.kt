package com.ykjm.todomap.todomap

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ykjm.todomap.todomap.adapter.TodoCalendarAdapter
import com.ykjm.todomap.todomap.db.ToDoEntity
import com.ykjm.todomap.todomap.viewmodels.TodoViewModel
import java.text.SimpleDateFormat
import java.util.*

class CalendarTodoActivity : AppCompatActivity() {

    private lateinit var dateTextView: TextView
    private lateinit var backButton: Button
    private lateinit var todoViewModel: TodoViewModel
    private lateinit var todoListAdapter: TodoCalendarAdapter // RecyclerView Adapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.calendar_todo)

        dateTextView = findViewById(R.id.dateTextView)
        backButton = findViewById(R.id.backButton)
        todoListAdapter = TodoCalendarAdapter()

        // ViewModel 초기화
        todoViewModel = ViewModelProvider(this).get(TodoViewModel::class.java)

        // RecyclerView 설정
        val recyclerView = findViewById<RecyclerView>(R.id.todoList)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = todoListAdapter

        backButton.setOnClickListener {
            finish()
        }

        val selectedDate = intent.getLongExtra("SELECTED_DATE", 0)
        displaySelectedDate(selectedDate)
        loadTodoList(selectedDate)
    }

    private fun displaySelectedDate(selectedDate: Long) {
        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(selectedDate))
        dateTextView.text = formattedDate
    }

    private fun loadTodoList(selectedDate: Long) {
        // 선택한 날짜로 저장된 투두 목록을 불러오는 메서드 호출
        val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(selectedDate))
        todoViewModel.getTodosByDate(formattedDate).observe(this, { todos ->
            todos?.let {
                todoListAdapter.submitList(it) // RecyclerView에 데이터 갱신
            }
        })
    }
}
