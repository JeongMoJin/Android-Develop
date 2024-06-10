package kr.ykjm.todomap.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TodoDao {
    @Insert
    suspend fun insertTodo(todoItem: TodoItem)

    @Query("SELECT * FROM TodoItem")
    suspend fun getAllTodos(): List<TodoItem>
}