package com.ykjm.todomap.todomap.repository

import androidx.lifecycle.LiveData
import com.ykjm.todomap.todomap.db.ToDoDao
import com.ykjm.todomap.todomap.db.ToDoEntity

class TodoRepository(private val todoDao: ToDoDao) {  // 데이터베이스와 통신하는 중개자 역할

    fun getAllTodos(): LiveData<List<ToDoEntity>> {
        return todoDao.getAllLiveData()
    }

    suspend fun insert(todo: ToDoEntity) {
        todoDao.insertTodo(todo)
    }

    suspend fun delete(todo: ToDoEntity) {
        todoDao.deleteTodo(todo)
    }

    suspend fun update(todo: ToDoEntity) {
        todoDao.updateTodo(todo)
    }

    fun getTodoById(id: Int): LiveData<ToDoEntity> {
        return todoDao.getTodoById(id)
    }
}