package kr.ykjm.todomap.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = arrayOf(ToDoEntity::class), version = 1)
abstract class AppDatebase : RoomDatabase(){
    abstract fun getTodoDao(): TodoDao

    companion object {
        val databaseName = "db_todo"
        var appDatebase : AppDatebase? = null

        fun getInstance(context: Context) : AppDatebase? {
            if(appDatebase == null) {
                appDatebase = Room.databaseBuilder(context,
                AppDatebase::class.java,
                databaseName).build()
            }
            return appDatebase
        }
    }
}