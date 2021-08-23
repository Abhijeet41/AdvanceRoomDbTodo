package com.abhi41.roomdbadvance.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.abhi41.roomdbadvance.roomdb.database.TodoDatabase;
import com.abhi41.roomdbadvance.roomdb.entity.Todo;

import org.jetbrains.annotations.NotNull;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class TodoNoteViewModel extends AndroidViewModel {
    private static final String TAG = "TodoNoteViewModel";
    TodoDatabase todoDatabase;
    public MutableLiveData<Boolean> mutable_isInserted = new MutableLiveData<>();
    public MutableLiveData<Boolean> mutable_isUpdated = new MutableLiveData<>();

    public TodoNoteViewModel(@NonNull @NotNull Application application) {
        super(application);
        todoDatabase = TodoDatabase.getTodoDB(application);
    }

    public void insert_todo(Todo todo){
        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(todoDatabase.todoDao().insertTodo(todo)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    mutable_isInserted.setValue(false);
                })
                .subscribe(() -> {
                    mutable_isInserted.setValue(true);
                    Log.d(TAG, "insert_todo: ");
                }));
    }

    public void update_todo(Todo todo){

        CompositeDisposable compositeDisposable = new CompositeDisposable();
        compositeDisposable.add(todoDatabase.todoDao().updateTodo(todo)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(throwable -> {
                    mutable_isUpdated.setValue(false);
                })
                .subscribe(() -> {
                    mutable_isUpdated.setValue(true);
                }));

    }

}
