package com.abhi41.roomdbadvance.Repository;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.abhi41.roomdbadvance.Adapter.TodoListAdapter;
import com.abhi41.roomdbadvance.roomdb.database.TodoDatabase;
import com.abhi41.roomdbadvance.roomdb.entity.Todo;
import com.abhi41.roomdbadvance.viewmodel.MainFragmentViewModel;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class MainFragmentRepository {
    private Application application;
    TodoDatabase todoDatabase;
    public List<Todo> todoArrayList = new ArrayList<>();
    public MutableLiveData<List<Todo>> tempArraylist = new MutableLiveData<>();
    public MutableLiveData<List<Todo>> mutabletodoArrayList = new MutableLiveData<>();
    public MutableLiveData<Boolean> isDeleted = new MutableLiveData<>();

    public MainFragmentRepository(Application application) {
        this.application = application;
        todoDatabase = TodoDatabase.getTodoDB(application);
    }

    public MutableLiveData<List<Todo>> getMutabletodoArrayList(){
        return tempArraylist;
    }

    public LiveData<List<Todo>> buildDummmyTodos() {
        todoArrayList.clear();
        Todo todo = new Todo();
        todo.setName("Android Retrofit Tutorial");
        todo.setDescription("Cover a tutorial on the Retrofit networking library using a RecyclerView to show the data.");
        todo.setCategory("Android");

        todoArrayList.add(todo);

        todo = new Todo();
        todo.setName("iOS TableView Tutorial");
        todo.setDescription("Covers the basics of TableViews in iOS using delegates.");
        todo.setCategory("iOS");

        todoArrayList.add(todo);

        todo = new Todo();
        todo.setName("iOS TableView Tutorial");
        todo.setDescription("Cover the concepts of Arrays in Kotlin and how they differ from the Java ones.");
        todo.setCategory("Kotlin");

        todoArrayList.add(todo);

        todo = new Todo();
        todo.setName("Swift Arrays");
        todo.setDescription("Cover the concepts of Arrays in Swift and how they differ from the Java and Kotlin ones.");
        todo.setCategory("Swift");

        todoArrayList.add(todo);

        mutabletodoArrayList.setValue(todoArrayList);

        insertTodoListDB(todoArrayList);

        return mutabletodoArrayList;
    }

    private void insertTodoListDB(List<Todo> todoArrayList) {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(todoDatabase.todoDao().insertTodoList(todoArrayList)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    throwable.printStackTrace();
                }).subscribe(() -> {

                    compositeDisposable.dispose();

                }));

    }

    public LiveData<List<Todo>> fetchAllTodos(){

        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(todoDatabase.todoDao().fetchTodos()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {})
                .subscribe(todoList -> {
                    todoArrayList.clear();

                    todoArrayList.addAll(todoList);
                    tempArraylist.setValue(todoList);
                    //compositeDisposable.dispose();
                }));
        return tempArraylist;
    }

    public LiveData<List<Todo>> fetchTodoByCategory(String category){

        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(todoDatabase.todoDao().fetchTodoByCategory(category)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    Log.d("error",throwable.getMessage());
                })
                .subscribe(todoList -> {
                    todoArrayList.clear();
                    todoArrayList.addAll(todoList);
                    tempArraylist.setValue(todoList);
                 //  compositeDisposable.dispose();
                }));

        return tempArraylist;

    }

    public void deleteTodo(Todo todo)
    {
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(todoDatabase.todoDao().deleteTodo(todo)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    isDeleted.setValue(false);
                })
                .subscribe(() -> {
                    isDeleted.setValue(true);
                }));
    }
}
