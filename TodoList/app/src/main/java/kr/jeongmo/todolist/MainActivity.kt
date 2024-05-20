package kr.jeongmo.todolist

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.jeongmo.todolist.databinding.ActivityAddTodoBinding
import kr.jeongmo.todolist.databinding.ActivityMainBinding
import kr.jeongmo.todolist.db.AppDatebase
import kr.jeongmo.todolist.db.ToDoDao
import kr.jeongmo.todolist.db.ToDoEntity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var db : AppDatebase
    private lateinit var todoDao : ToDoDao
    private lateinit var todoList : ArrayList<ToDoEntity>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnAdd.setOnClickListener {
            val intent = Intent(this, AddTodoActivity::class.java)
            startActivity(intent)
        }

        // DB 인스턴스를 가져오고 DB작업을 할 수 있는 DAO를 가져옵니다.
        db = AppDatebase.getInstance(this)!!
        todoDao = db.getTodoDao()

        getAllTodoList() // 할 일 리스트 가져오기
    }

    private fun getAllTodoList() {
        Thread {
            todoList = ArrayList(todoDao.getAll())
            setRecyclerView()
        }.start()
    }

    private fun setRecyclerView() {

    }
}

















