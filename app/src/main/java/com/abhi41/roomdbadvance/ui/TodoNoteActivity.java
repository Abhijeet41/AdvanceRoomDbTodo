package com.abhi41.roomdbadvance.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.abhi41.roomdbadvance.MainActivity;
import com.abhi41.roomdbadvance.R;
import com.abhi41.roomdbadvance.databinding.ActivityTodoNoteBinding;
import com.abhi41.roomdbadvance.roomdb.entity.Todo;
import com.abhi41.roomdbadvance.viewmodel.TodoNoteViewModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;

public class TodoNoteActivity extends AppCompatActivity {

    ActivityTodoNoteBinding binding;


    String strTitle, strDescription, strCategory;

    private String[] categories = {
            "Android",
            "iOS",
            "Kotlin",
            "Swift"
    };
    public ArrayList<String> spinnerList = new ArrayList<>(Arrays.asList(categories));
    private TodoNoteViewModel viewModel;
    private int todoId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTodoNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new TodoNoteViewModel(getApplication());

        Intent intent = getIntent();

        if (intent != null) {

            try {
                Todo todo = intent.getParcelableExtra("updateTodo");

                todoId = intent.getIntExtra("todoId",-1);

                binding.edtTitle.getEditText().setText(todo.getCategory());
                binding.edtDescription.getEditText().setText(todo.getDescription());
                binding.spinner.setSelection(spinnerList.indexOf(todo.getCategory()));


                binding.btnDone.setText("Update");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinner.setAdapter(adapter);

        binding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        binding.btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strTitle = binding.edtTitle.getEditText().getText().toString();
                strDescription = binding.edtDescription.getEditText().getText().toString();

                if (!strCategory.equals("") && !strDescription.equals("") && !strCategory.equals("")
                        && strCategory != null && strDescription != null && strCategory != null) {


                    if (binding.btnDone.getText().equals("Done"))
                    {

                            Todo todo = new Todo();

                            todo.setName(strTitle);
                            todo.setDescription(strDescription);
                            todo.setCategory(strCategory);

                            viewModel.insert_todo(todo);



                    }else if (binding.btnDone.getText().equals("Update")) {

                   /*     if (todoId != -1)
                        {
                            return;
                        }*/
                        Log.d("todoId: ",String.valueOf(todoId));

                        Todo todoUpdate = new Todo();
                        todoUpdate.setTodo_id(todoId);
                        todoUpdate.setCategory(strCategory);
                        todoUpdate.setName(binding.edtTitle.getEditText().getText().toString());
                        todoUpdate.setDescription(binding.edtDescription.getEditText().getText().toString());

                        if (todoUpdate != null) {
                            Toast.makeText(TodoNoteActivity.this, "update", Toast.LENGTH_SHORT).show();
                            viewModel.update_todo(todoUpdate);
                        } else {
                            Toast.makeText(TodoNoteActivity.this, "please enter all field", Toast.LENGTH_SHORT).show();
                        }
                    }
                    binding.edtTitle.getEditText().getText().clear();
                    binding.edtDescription.getEditText().getText().clear();
                } else {
                    Toast.makeText(TodoNoteActivity.this, "please enter all field", Toast.LENGTH_SHORT).show();
                }
            }
        });

        observable();

    }

    private void observable() {
        viewModel.mutable_isInserted.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    Toast.makeText(TodoNoteActivity.this, "Data Inserted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(TodoNoteActivity.this, MainActivity.class));
                } else {
                    Toast.makeText(TodoNoteActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewModel.mutable_isUpdated.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean)
                {

                    Toast.makeText(TodoNoteActivity.this, "Update successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(TodoNoteActivity.this,MainActivity.class);
                    startActivity(intent);

                    Bundle bundle = new Bundle();
                    bundle.putString("update", "true");
// set Fragmentclass Arguments
                    MainFragment MainFragment = new MainFragment();
                    MainFragment.setArguments(bundle);
                }else {
                    Toast.makeText(TodoNoteActivity.this, "somethig went wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}