package com.example.g05fitstore.Models;

public class SensorModel {
    private String id;  // Firebase sẽ tự tạo id cho bạn
    private String content;
    private String sensor;
    private String timestamp;

    // Cần phải có constructor mặc định để Firebase có thể tạo đối tượng từ dữ liệu trả về từ database
    public SensorModel() {
    }

    public SensorModel(String content, String sensor, String timestamp) {
        this.content = content;
        this.sensor = sensor;
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

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
