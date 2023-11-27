package com.example.g05fitstore.Models;

public class Feedback {
    private String id;  // Firebase sẽ tự tạo id cho bạn
    private String content;
    private int num;
    private String timestamp;

    // Cần phải có constructor mặc định để Firebase có thể tạo đối tượng từ dữ liệu trả về từ database
    public Feedback() {
    }

    public Feedback(String content, int num, String timestamp) {
        this.content = content;
        this.num = num;
        this.timestamp = timestamp;
    }

    // Các phương thức getter và setter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

