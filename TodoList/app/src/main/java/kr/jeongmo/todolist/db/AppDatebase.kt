package kr.jeongmo.todolist.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(ToDoEntity::class), version = 1)
abstract class AppDatebase : RoomDatabase() {
    abstract fun getTodoDao() : ToDoDao

    companion object {
        val databaseName = "db_todo"
        var appDatabase : AppDatebase? = null

        fun getInstance(context: Context) : AppDatebase? {
            if (appDatabase == null) {
                appDatabase = Room.databaseBuilder(context,
                AppDatebase::class.java,
                databaseName).build()
            }
            return appDatabase
        }
    }
}