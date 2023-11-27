package com.example.g05fitstore.Client;
import static java.security.AccessController.getContext;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.g05fitstore.Models.Feedback;
import com.example.g05fitstore.R;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FeedbackActivity extends AppCompatActivity {

    private EditText contentEditText, numEditText;
    private Button submitButton , feedbackList;

    private DatabaseReference feedbackRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        // Khởi tạo DatabaseReference để tham chiếu đến "Feedback" trên Firebase
        feedbackRef = FirebaseDatabase.getInstance().getReference("Feedback");

        contentEditText = findViewById(R.id.editTextContent);
        numEditText = findViewById(R.id.editTextNum);
        submitButton = findViewById(R.id.buttonSubmit);
        feedbackList = findViewById(R.id.List);

        feedbackList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),FeedbackListActivity.class);
                startActivities(new Intent[]{intent});
            }
        });
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitFeedback();
            }
        });
    }

    private void submitFeedback() {
        // Lấy dữ liệu từ giao diện người dùng
        String content = contentEditText.getText().toString();
        String numStr = numEditText.getText().toString();

        if (content.isEmpty() || numStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        int num = Integer.parseInt(numStr);
          // Lấy thời gian hiện tại
        String timestamp = String.valueOf(System.currentTimeMillis());
        // Tạo một đối tượng Feedback mới
        Feedback feedback = new Feedback(content, num, timestamp);

        // Thêm đối tượng Feedback vào database
        String feedbackId = feedbackRef.push().getKey(); // Tạo một id mới cho phản hồi
        feedback.setId(feedbackId);

        feedbackRef.child(feedbackId).setValue(feedback, (DatabaseReference.CompletionListener) (error, ref) -> {
            if (error == null) {
                Toast.makeText(FeedbackActivity.this, "Phản hồi đã được gửi thành công", Toast.LENGTH_SHORT).show();
                // Sau khi gửi thành công, bạn có thể thực hiện các hành động khác ở đây
            } else {
                Toast.makeText(FeedbackActivity.this, "Lỗi khi gửi phản hồi: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
