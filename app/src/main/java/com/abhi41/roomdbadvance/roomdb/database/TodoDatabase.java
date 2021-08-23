package com.abhi41.roomdbadvance.roomdb.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.abhi41.roomdbadvance.roomdb.dao.TodoDao;
import com.abhi41.roomdbadvance.roomdb.entity.Todo;


@Database(entities = Todo.class, version = 1, exportSchema = false)
public abstract class TodoDatabase extends RoomDatabase {

    private static TodoDatabase todoDatabase;

    public static synchronized TodoDatabase getTodoDB(Context context) {

        if (todoDatabase == null) {
            todoDatabase = Room.databaseBuilder(context, TodoDatabase.class, "toto_db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return todoDatabase;

    }
    public abstract TodoDao todoDao();

}
