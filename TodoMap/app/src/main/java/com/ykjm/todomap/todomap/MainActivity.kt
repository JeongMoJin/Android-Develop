package com.ykjm.todomap.todomap

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils.replace

import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.ykjm.todomap.todomap.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.todo_list)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavigationView = findViewById(R.id.bottom_Navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.list_bottom -> {
                    // 목록 화면
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, TodoListFragment()) // TodoListFragment() 이름 교체
                        .commit()
                    true
                }
                R.id.cal_bottom -> {
                    // 달력 화면
//                    supportFragmentManager.beginTransaction()
//                        .replace(R.id.fragment_container, TodoCalendarFragment()) // TodoCalendarFragment() 이름 교체
//                        .commit()
                    val intent = Intent(this, CalendarActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.add_bottom -> {
                    // 추가 화면
                    AddTodoFragment().show(supportFragmentManager, "AddTodoFragment")
                    true
                }
                R.id.map_bottom -> {
                    // 지도 화면
                    val intent = Intent(this, MapsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.sys_bottom -> {
                    // 설정 화면
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, TodoSystemFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }
        // 기본 페이지
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TodoListFragment())
                .commit()
        }
    }
}