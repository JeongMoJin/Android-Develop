package kr.ykjm.todomap.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TodoItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val date: String,
    val emotion: String,
    val todoText: String

)
