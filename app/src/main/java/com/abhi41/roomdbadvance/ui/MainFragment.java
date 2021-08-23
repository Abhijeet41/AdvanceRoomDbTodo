package com.abhi41.roomdbadvance.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.navigation.fragment.NavHostFragment;

import com.abhi41.roomdbadvance.Adapter.TodoListAdapter;
import com.abhi41.roomdbadvance.Interface.Todolist_callback;
import com.abhi41.roomdbadvance.R;
import com.abhi41.roomdbadvance.databinding.FragmentFirstBinding;
import com.abhi41.roomdbadvance.roomdb.entity.Todo;
import com.abhi41.roomdbadvance.viewmodel.MainFragmentViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MainFragment extends Fragment implements Todolist_callback {

    private FragmentFirstBinding binding;
    private MainFragmentViewModel viewModel;
    private TodoListAdapter todoListAdapter;

    private String[] categories = {
            "All",
            "Android",
            "iOS",
            "Kotlin",
            "Swift"
    };
    ArrayList<String> spinnerList = new ArrayList<>(Arrays.asList(categories));
    ArrayList<Todo> todoArrayList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        viewModel = new MainFragmentViewModel(getActivity().getApplication());

        checkIfAppLaunchedFirstTime();


        try {
            Bundle bundle = getArguments();
            if (bundle!= null)
            {
                String update = bundle.getString("update","none");
                if (update.equals("true"))
                {
                    todoListAdapter.notifyDataSetChanged();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }



        return binding.getRoot();


    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();

        //  observable();

    }


    private void initView() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (getActivity(), R.layout.dropdown_item, spinnerList);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.autoComplete.setAdapter(adapter);

        binding.autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {

                    viewModel.fetchAllTodos().observe(getActivity(), new Observer<List<Todo>>() {
                        @Override
                        public void onChanged(List<Todo> todoList) {
                            todoArrayList.clear();
                            todoArrayList.addAll(todoList);
                            updateRecyclerView(todoArrayList);
                        }
                    });

                } else {
                    viewModel.fetchTodoByCategory(parent.getItemAtPosition(position).toString())
                            .observe(getActivity(), new Observer<List<Todo>>() {
                                @Override
                                public void onChanged(List<Todo> todoList) {
                                    todoArrayList.clear();
                                    todoArrayList.addAll(todoList);

                                    updateRecyclerView(todoArrayList);
                                }
                            });

                }
                Toast.makeText(getActivity(), "" + parent.getItemAtPosition(position), Toast.LENGTH_SHORT).show();
            }
        });


        //bind recyclerview
        todoListAdapter = new TodoListAdapter(getActivity(), todoArrayList,this);
        binding.rvTodos.setHasFixedSize(true);
        binding.rvTodos.setAdapter(todoListAdapter);
        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),TodoNoteActivity.class);
                startActivity(intent);
            }
        });

    }

    private void updateRecyclerView(ArrayList<Todo> todoArrayList) {

        todoListAdapter = new TodoListAdapter(getActivity(), todoArrayList,this);
        binding.rvTodos.setHasFixedSize(true);
        binding.rvTodos.setAdapter(todoListAdapter);
        todoListAdapter.notifyDataSetChanged();
    }

    private void observable() {


        viewModel.buildDummmyTodos().observe(getActivity(), new Observer<List<Todo>>() {
            @Override
            public void onChanged(List<Todo> todoList) {
                todoArrayList.clear();
                todoArrayList.addAll(todoList);

                updateRecyclerView(todoArrayList);
            }
        });

        viewModel.isDeleted().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                {
                    todoListAdapter.notifyDataSetChanged();
                    Toast.makeText(getActivity(), "Item deleted", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(), "something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void checkIfAppLaunchedFirstTime() {
        final String PREFS_NAME = "SharedPrefs";

        SharedPreferences settings = getActivity().getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("firstTime", true)) {
            settings.edit().putBoolean("firstTime", false).apply();
            observable();
        }
    }

    @Override
    public void deleteTodo(Todo todo) {
        viewModel.deleteTodo(todo);
    }

    @Override
    public void updateTodo(Todo todo, int todoId) {
        Intent intent = new Intent(getActivity(),TodoNoteActivity.class);
        intent.putExtra("updateTodo",(Parcelable) todo);
        intent.putExtra("todoId",todoId);
        startActivity(intent);
    }

    public  void updateDatabase (){
        todoListAdapter.notifyDataSetChanged();
    }
}