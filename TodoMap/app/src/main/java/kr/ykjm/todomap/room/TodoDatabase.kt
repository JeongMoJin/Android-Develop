package kr.ykjm.todomap.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [TodoItem::class], version = 1)
abstract class TodoDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}