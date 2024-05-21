package kr.jeongmo.todolist

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.jeongmo.todolist.databinding.ItemTodoBinding
import kr.jeongmo.todolist.db.ToDoEntity

class TodoRecyclerViewAdapter(private val todoList: ArrayList<ToDoEntity>,
private val listener: OnItemLongClickListener):
    RecyclerView.Adapter<TodoRecyclerViewAdapter.MyViewHolder>() {

    inner class MyViewHolder(binding: ItemTodoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val tv_importanc = binding.tvImportance
        val tv_title = binding.tvTitle

        // 뷰 바인딩에서 기본적으로 제공하는 root 변수는 레이아웃의
        // 루트 레이아웃을 의미합니다.
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        // item_todo.xml 뷰 바인딩 객체 생성
        val binding: ItemTodoBinding =
            ItemTodoBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val todoData = todoList[position]
        // 중요도에 따라 색상을 변경
        when (todoData.importance) {
            1 -> {
                holder.tv_importanc.setBackgroundResource(R.color.red)
            }
            2 -> {
                holder.tv_importanc.setBackgroundResource(R.color.yellow)
            }
            3 -> {
                holder.tv_importanc.setBackgroundResource(R.color.green)
            }
        }
        // 중요도에 따라 중요도 텍스트(1, 2, 3) 변경
        holder.tv_importanc.text = todoData.importance.toString()
        // 할 일의 제목 변경
        holder.tv_title.text = todoData.title
        // 할 일이 길게 클릭되었을 때 리스너 함수 실행
        holder.root.setOnLongClickListener {
            listener.onLongClick(position)
            false
        }
    }

    override fun getItemCount(): Int {
        // 리사이클러뷰 아이템
        return todoList.size
    }


}