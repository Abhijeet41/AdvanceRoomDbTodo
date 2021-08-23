package com.abhi41.roomdbadvance.roomdb.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity (tableName = "Todo")
public class Todo implements Parcelable {

    @PrimaryKey (autoGenerate = true)
    private int todo_id;

    private String description;

    private String category;

    private String name;


    public Todo() {
    }

    public Todo(Parcel in) {
        todo_id = in.readInt();
        description = in.readString();
        category = in.readString();
        name = in.readString();
        priority = in.readString();
    }

    public static final Creator<Todo> CREATOR = new Creator<Todo>() {
        @Override
        public Todo createFromParcel(Parcel in) {
            return new Todo(in);
        }

        @Override
        public Todo[] newArray(int size) {
            return new Todo[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Ignore
    public String priority;

    public int getTodo_id() {
        return todo_id;
    }

    public void setTodo_id(int todo_id) {
        this.todo_id = todo_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(todo_id);
        dest.writeString(description);
        dest.writeString(category);
        dest.writeString(name);
        dest.writeString(priority);
    }
}
