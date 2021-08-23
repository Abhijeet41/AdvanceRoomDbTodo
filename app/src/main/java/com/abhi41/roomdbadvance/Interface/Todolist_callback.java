package com.abhi41.roomdbadvance.Interface;

import com.abhi41.roomdbadvance.roomdb.entity.Todo;

public interface Todolist_callback {
    void deleteTodo(Todo todo);

    void updateTodo(Todo todo,int id);
}
