package com.example.g05fitstore.Models;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Product {
    private String id;
    private String userId;
    private String name;
    private String image;
    private String desc;
    private Integer price;
    private String timestamp;

    public Product(String id, String userId, String name, String image, String desc, Integer price, String timestamp) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.image = image;
        this.desc = desc;
        this.price = price;
        this.timestamp = timestamp;
    }


    public Product(){

    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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


    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("desc", desc);
        result.put("image", image);
        result.put("price", price);
        result.put("timestamp", timestamp);
        return result;
    }
}
