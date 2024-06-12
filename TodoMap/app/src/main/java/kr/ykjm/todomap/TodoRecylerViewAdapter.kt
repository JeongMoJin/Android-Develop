package kr.ykjm.todomap

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.ykjm.todomap.databinding.ItemTodoBinding
import kr.ykjm.todomap.db.ToDoEntity

class TodoRecylerViewAdapter(private val todoList: ArrayList<ToDoEntity>)
    :RecyclerView.Adapter<TodoRecylerViewAdapter.MyViewHolder>(){

        inner class MyViewHolder(binding: ItemTodoBinding):
            RecyclerView.ViewHolder(binding.root) {
            val iv_check = binding.btnNotCheck
            val tv_title=binding.todoNotClear

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
        when (todoData.check) {
            true -> {
                holder.iv_check.setImageResource(R.drawable.ic_baseline_check_circle_24)
            }
            false -> {
                holder.iv_check.setImageResource(R.drawable.ic_outline_circle_24)
            }
        }
    }

    override fun getItemCount(): Int {
        return todoList.size
    }
}