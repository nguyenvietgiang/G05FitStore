package com.example.g05fitstore.Models;

import java.util.HashMap;
import java.util.Map;

public class Product {
    private String id;
    private String name;
    private String image;
    private String desc;

    public Product(String id, String name, String image, String desc) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.desc = desc;
    }
    public Product(){

    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getDesc() {
        return desc;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("desc", desc);
        result.put("image", image);

        return result;
    }
}
