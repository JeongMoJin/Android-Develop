package kr.ykjm.todomap.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TodoDao {
    @Query("SELECT * FROM ToDoEntity")
    fun getAll() : List<ToDoEntity>

    @Insert
    fun insertEmotion(emotion: EmotionEntity)
}