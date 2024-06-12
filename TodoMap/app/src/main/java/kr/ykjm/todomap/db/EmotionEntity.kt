package kr.ykjm.todomap.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class EmotionEntity (
    @PrimaryKey(autoGenerate = true) var id:Int? = null,
    @ColumnInfo(name="emotion") var emotion:String? = null
)