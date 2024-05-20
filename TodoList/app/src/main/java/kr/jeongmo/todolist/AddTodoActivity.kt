package kr.jeongmo.todolist

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kr.jeongmo.todolist.databinding.ActivityAddTodoBinding
import kr.jeongmo.todolist.db.AppDatebase
import kr.jeongmo.todolist.db.ToDoDao
import kr.jeongmo.todolist.db.ToDoEntity

class AddTodoActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddTodoBinding
    lateinit var db : AppDatebase
    lateinit var todoDao: ToDoDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatebase.getInstance(this)!!
        todoDao = db.getTodoDao()

        binding.btnCompletion.setOnClickListener {
            insertTodo()
        }
    }

    /**
     * @desc 할 일 추가 함수
     */
    private fun insertTodo() {
        val todoTitle = binding.edtTitle.text.toString() // 할 일의 제목
        var todoImportance = binding.radioGroup.checkedRadioButtonId // 할 일의 중요도

        // 어떤 버튼이 눌렸는지 확인하고 값을 지정해줍니다
        when(todoImportance) {
            R.id.btn_high -> {
                todoImportance = 1
            }
            R.id.btn_middle -> {
                todoImportance = 2
            }
            R.id.btn_low -> {
                todoImportance = 3
            }
            else -> {
                todoImportance = -1
            }
        }
        // 중요도가 선택되지 않았거나, 제목이 작성되지 않는지 체크합니다.
        if (todoImportance == -1 || todoTitle.isBlank()) {
            Toast.makeText(this, "모든 항목을 채워주세요", Toast.LENGTH_SHORT).show()
        } else {
            Thread {
                todoDao.insertTodo(ToDoEntity(null, todoTitle, todoImportance))
                runOnUiThread {
                    Toast.makeText(this, "추가되었습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }.start()
        }















    }
}