package com.abhi41.roomdbadvance.roomdb.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


import com.abhi41.roomdbadvance.roomdb.entity.Todo;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Single;


@Dao
public interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTodo(Todo todo);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertTodoList(List<Todo> todoList);

    @Query("SELECT * FROM Todo")
    Flowable<List<Todo>> fetchTodos();

    @Query("SELECT * FROM Todo WHERE " +
            "category = :category")
    Flowable<List<Todo>> fetchTodoByCategory(String category);

    @Query("SELECT * FROM Todo WHERE todo_id = :todo")
    Single<Todo> fetchTodoById(int todo);

    @Update
    Completable updateTodo(Todo todo);

    @Delete
    Completable deleteTodo(Todo todo);

    @Query("DELETE FROM Todo")
    Void deleteAll();

}
