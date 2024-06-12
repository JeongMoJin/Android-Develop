package kr.ykjm.todomap

import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import kr.ykjm.todomap.databinding.CalendarTodoBinding
import kr.ykjm.todomap.db.AppDatebase
import kr.ykjm.todomap.db.ToDoEntity
import kr.ykjm.todomap.db.TodoDao

class CalendarTodoActivity : AppCompatActivity() {

    private lateinit var binding : CalendarTodoBinding

    private lateinit var db : AppDatebase
    private lateinit var todoDao : TodoDao
    private lateinit var todoList: ArrayList<ToDoEntity>
    private lateinit var adapter: TodoRecylerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = CalendarTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // DB 인스턴스를 가져오고 DB작업을 할 수 있는 DAO를 가져옵니다.
        db = AppDatebase.getInstance(this)!!
        todoDao = db.getTodoDao()

        getAllTodoList() // 할 일 리스트 가져오기
    }
    private fun getAllTodoList() {
        Thread {
            todoList = ArrayList(todoDao.getAll())
            setRecylerView()
        }.start()
    }

    private fun setRecylerView() {
        // 리사이클러뷰 설정
        runOnUiThread {
            adapter = TodoRecylerViewAdapter(todoList)
            binding.recyclerView.adapter = adapter
            binding.recyclerView.layoutManger = LinearLayoutManager(this)
        }
    }
}