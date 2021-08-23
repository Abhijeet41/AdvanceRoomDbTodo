package com.abhi41.roomdbadvance.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.abhi41.roomdbadvance.Interface.Todolist_callback;
import com.abhi41.roomdbadvance.R;
import com.abhi41.roomdbadvance.databinding.SingleTodoLayoutBinding;
import com.abhi41.roomdbadvance.roomdb.entity.Todo;
import com.chauthai.swipereveallayout.ViewBinderHelper;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.ViewHolder> {
    private LayoutInflater inflater;
    private Context context;
    List<Todo> todoArrayList;
    private final ViewBinderHelper viewBinderHelper = new ViewBinderHelper();
    private Todolist_callback todolistCallback;

    public TodoListAdapter(Context context, List<Todo> todoArrayList,Todolist_callback todolistCallback) {
        this.context = context;
        this.todoArrayList = todoArrayList;
        this.todolistCallback = todolistCallback;
    }

    @NonNull
    @NotNull
    @Override
    public TodoListAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
       // View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_todo_layout,parent,false);
        if (inflater == null) {
            inflater = LayoutInflater.from(parent.getContext());
        }
        SingleTodoLayoutBinding binding = SingleTodoLayoutBinding.inflate(inflater,parent,false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull TodoListAdapter.ViewHolder holder, int position) {

        final Todo todo = todoArrayList.get(position);
        holder.itemView.txtName.setText(todo.getName());
        holder.itemView.txtCategory.setText(todo.getCategory());
        holder.itemView.txtDesc.setText(todo.getDescription());
        holder.itemView.txtNo.setText("#" + String.valueOf(todo.getTodo_id()));

        viewBinderHelper.setOpenOnlyOne(true);
        viewBinderHelper.bind(holder.itemView.swipelayout,todo.getName());
        viewBinderHelper.closeLayout(todo.getName());

        holder.itemView.txtDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Delete", Toast.LENGTH_SHORT).show();
                todolistCallback.deleteTodo(todo);

            }
        });

        holder.itemView.txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Edit", Toast.LENGTH_SHORT).show();
                todolistCallback.updateTodo(todo,todo.getTodo_id());
            }
        });

    }

    @Override
    public int getItemCount() {
        return todoArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SingleTodoLayoutBinding itemView;

        public ViewHolder(@NonNull @NotNull SingleTodoLayoutBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
        }
    }

}
