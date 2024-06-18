package com.ykjm.todomap.todomap.db

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ToDoDao {

    @Query("SELECT * FROM ToDoEntity")
    fun getAllLiveData(): LiveData<List<ToDoEntity>> // LiveData로 변경

    @Query("SELECT * FROM ToDoEntity WHERE id = :id")
    fun getTodoById(id: Int): LiveData<ToDoEntity> // LiveData로 변경

    @Insert
    fun insertTodo(todo: ToDoEntity)

    @Delete
    fun deleteTodo(todo: ToDoEntity)

    @Update
    fun updateTodo(todo: ToDoEntity)

}