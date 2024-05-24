package kr.jeongmo.roomstudy.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity // 어떤 구성요소인지를 알려주려면 꼭 어노테이션을 써주어야 합니다.
data class ToDoEntity ( // 중괄호 아닌 소괄호입니다.
    @PrimaryKey(autoGenerate = true) var id : Int? = null,
    @ColumnInfo(name="title") val title : String,
    @ColumnInfo(name="importance") val importance : Int
        )