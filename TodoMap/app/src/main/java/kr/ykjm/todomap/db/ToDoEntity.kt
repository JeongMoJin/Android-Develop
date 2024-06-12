package kr.ykjm.todomap.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
class ToDoEntity (
    @PrimaryKey(autoGenerate = true) var id: Int? = null,
    @ColumnInfo(name="check") val check : Boolean,
    @ColumnInfo(name="title") val title : String,
    @ColumnInfo(name="date") val date: Date
    )