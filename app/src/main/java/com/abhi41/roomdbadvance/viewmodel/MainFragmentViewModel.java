package com.abhi41.roomdbadvance.viewmodel;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.abhi41.roomdbadvance.Repository.MainFragmentRepository;
import com.abhi41.roomdbadvance.roomdb.database.TodoDatabase;
import com.abhi41.roomdbadvance.roomdb.entity.Todo;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;

public class MainFragmentViewModel extends AndroidViewModel {
    private Context context;
    TodoDatabase todoDatabase;
    private MainFragmentRepository mainFragmentRepository;

    public MainFragmentViewModel(@NonNull @NotNull Application application) {
        super(application);
        this.context = application;
        todoDatabase = TodoDatabase.getTodoDB(application);
        mainFragmentRepository = new MainFragmentRepository(application);
    }

    public LiveData<List<Todo>> buildDummmyTodos() {
        return mainFragmentRepository.buildDummmyTodos();
    }

    public MutableLiveData<Boolean> isDeleted() {
        return mainFragmentRepository.isDeleted;
    }

    //fetch all the todo records
    public LiveData<List<Todo>> fetchAllTodos() {
        return mainFragmentRepository.fetchAllTodos();
    }

    //fetch todo recored by category

    public LiveData<List<Todo>> fetchTodoByCategory(String category) {
        return mainFragmentRepository.fetchTodoByCategory(category);
    }

    //delete todo

    public void deleteTodo(Todo todo) {
        mainFragmentRepository.deleteTodo(todo);
    }

}
