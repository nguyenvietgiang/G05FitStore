package com.example.g05fitstore.Models;

import android.provider.ContactsContract;

import java.sql.Timestamp;
import java.util.Date;

public class Category {
    public String Id;
    public String Name;
    public Boolean Status;

    public Category() {
    }

    public Category(String id, String name, Boolean status) {
        Id = id;
        Name = name;
        Status = status;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }
}
