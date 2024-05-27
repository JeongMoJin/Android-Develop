package kr.jeongmo.roomstudy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kr.jeongmo.roomstudy.databinding.ActivityAddTodoBinding
import kr.jeongmo.roomstudy.db.AppDatabase
import kr.jeongmo.roomstudy.db.ToDoDao
import kr.jeongmo.roomstudy.db.ToDoEntity

class AddTodoActivity : AppCompatActivity() {

    lateinit var binding: ActivityAddTodoBinding
    lateinit var db : AppDatabase
    lateinit var todoDao : ToDoDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = AppDatabase.getInstance(this)!!
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
        var todoImpotance = binding.radioGroup.checkedRadioButtonId
        // 할 일의 중요도

        // 어떤 버튼이 눌렸는지 확인하고 값을 지정해줍니다
        when(todoImpotance) {
            R.id.btn_high -> {
                todoImpotance = 1
            }
            R.id.btn_middle -> {
                todoImpotance = 2
            }
            R.id.btn_low -> {
                todoImpotance = 3
            }
            else -> {
                todoImpotance = -1
            }
        }
        // 중요도가 선택되지 않았거나, 제목이 작성되지 않는지 체크합니다.
        if (todoImpotance == -1 || todoTitle.isBlank()) {
            Toast.makeText(this, "모든 항목을 채워주세요.",
                Toast.LENGTH_SHORT).show()
        } else {
            Thread {
                todoDao.insertTodo(ToDoEntity(null, todoTitle, todoImpotance))
                runOnUiThread { // 아래 작업은 UI 스레드에서 실행해주어야 합니다.
                    Toast.makeText(this, "추가되었습니다.",
                        Toast.LENGTH_SHORT).show()
                    finish() // AddTodoActivity 종료, 다시 MainActivity로 돌아감
                }
            }.start()
        }
    }
}




























