package com.example.g05fitstore.Client;

import android.content.Intent;
import android.hardware.Sensor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.g05fitstore.Models.Feedback;
import com.example.g05fitstore.Models.SensorModel;
import com.example.g05fitstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SaveActivity extends AppCompatActivity {
    FirebaseAuth auth;
    private EditText contentEditText;
    private Button submitButton;
    FirebaseUser user;
    private DatabaseReference feedbackRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save);

        feedbackRef = FirebaseDatabase.getInstance().getReference("Sensor");
        auth = FirebaseAuth.getInstance();
        submitButton = findViewById(R.id.buttonSubmit);
        user = auth.getCurrentUser();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitFeedback();
            }
        });
    }

    private void submitFeedback() {
        String content = user.getEmail()+"";
        String timestamp = String.valueOf(System.currentTimeMillis());
        String totalValue = getIntent().getStringExtra("lightValue");
        SensorModel sensor = new SensorModel(content, totalValue, timestamp);
        String feedbackId = feedbackRef.push().getKey(); // Tạo một id mới cho phản hồi
        sensor .setId(feedbackId);

        feedbackRef.child(feedbackId).setValue(sensor , (DatabaseReference.CompletionListener) (error, ref) -> {
            if (error == null) {
                Toast.makeText(SaveActivity.this, "Thông tin đã được lưu thành công", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(SaveActivity.this, "Lỗi khi gửi thông tin: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}