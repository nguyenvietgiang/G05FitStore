package com.example.g05fitstore.Client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import android.view.View;
import com.example.g05fitstore.Adaper.FeedbackAdapter;
import com.example.g05fitstore.Models.Feedback;
import com.example.g05fitstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class FeedbackListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FeedbackAdapter feedbackAdapter;
    private List<Feedback> feedbackList;
    Button sortHighToLow, sortLowToHigh, btnSortNewestFirst , btnSortOldestFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback_list);

        // Khởi tạo RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        sortHighToLow = findViewById(R.id.btnSortHighToLow);
        sortLowToHigh = findViewById(R.id.btnSortLowToHigh);
        btnSortNewestFirst = findViewById(R.id.btnSortNewestFirst);
        btnSortNewestFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortNewestFirst(v);
            }
        });
        btnSortOldestFirst = findViewById(R.id.btnSortOldestFirst);
        btnSortOldestFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sortOldestFirst(v);
            }
        });

        // Khởi tạo danh sách Feedback
        feedbackList = new ArrayList<>();

        // Khởi tạo Adapter
        feedbackAdapter = new FeedbackAdapter(this, feedbackList);

        // Thiết lập LayoutManager cho RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Thiết lập Adapter cho RecyclerView
        recyclerView.setAdapter(feedbackAdapter);

        // gọi sự kiện xóa trong adapter
        feedbackAdapter.setOnDeleteClickListener(new FeedbackAdapter.OnDeleteClickListener() {
            @Override
            public void onDeleteClick(int position) {
                // Xử lý sự kiện xóa tại vị trí 'position'
                deleteItem(position);
            }
        });

        // Khởi tạo Firebase Database
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Feedback");

        // Thực hiện truy vấn để lấy dữ liệu
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                feedbackList.clear();
                 // vòng for để duyệt qua toàn bộ danh sách trong CSDL
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Feedback feedback = snapshot.getValue(Feedback.class);
                    feedbackList.add(feedback);
                }
                // Định dạng lại timestamp trong danh sách feedbackList
                formatTimestamps();
                // Thông báo cho Adapter rằng dữ liệu đã thay đổi
                feedbackAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void sortHighToLow(View view) {
        Collections.sort(feedbackList, new Comparator<Feedback>() {
            @Override
            public int compare(Feedback feedback1, Feedback feedback2) {
                // Sắp xếp từ cao đến thấp
                return Integer.compare(feedback2.getNum(), feedback1.getNum());
            }
        });

        // Thông báo cho Adapter rằng dữ liệu đã thay đổi
        feedbackAdapter.notifyDataSetChanged();
    }

    // Phương thức để lọc từ thấp đến cao
    public void sortLowToHigh(View view) {
        Collections.sort(feedbackList, new Comparator<Feedback>() {
            @Override
            public int compare(Feedback feedback1, Feedback feedback2) {
                // Sắp xếp từ thấp đến cao
                return Integer.compare(feedback1.getNum(), feedback2.getNum());
            }
        });

        // Thông báo cho Adapter rằng dữ liệu đã thay đổi
        feedbackAdapter.notifyDataSetChanged();
    }

    private void formatTimestamps() {
        for (Feedback feedback : feedbackList) {
            // Định dạng timestamp thành dd/MM/yyyy
            String formattedDate = formatDate(feedback.getTimestamp());
            feedback.setTimestamp(formattedDate);
        }
    }

    private String formatDate(String timestamp) {
        // Định dạng timestamp từ kiểu nguyên thủy sang kiểu dd/MM/yyyy
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date date = new Date(Long.parseLong(timestamp));
        return sdf.format(date);
    }
    // gọi xóa
    private void deleteItem(int position) {
        // Xóa mục tại vị trí 'position' trong danh sách và cập nhật Adapter
        feedbackList.remove(position);
        feedbackAdapter.notifyItemRemoved(position);
    }

    // Phương thức để sắp xếp theo mới nhất
    public void sortNewestFirst(View view) {
        for (Feedback feedback : feedbackList) {
            Log.d("Timestamp", "Formatted: " + feedback.getTimestamp() + ", Original: " + feedback.getTimestamp());
        }

        Collections.sort(feedbackList, new Comparator<Feedback>() {
            @Override
            public int compare(Feedback feedback1, Feedback feedback2) {
                // Sắp xếp từ mới nhất đến cũ nhất
                return Long.compare(Long.parseLong(feedback2.getTimestamp()), Long.parseLong(feedback1.getTimestamp()));
            }
        });

        // Thông báo cho Adapter rằng dữ liệu đã thay đổi
        feedbackAdapter.notifyDataSetChanged();
    }

    // Phương thức để sắp xếp theo cũ nhất
    public void sortOldestFirst(View view) {
        Collections.sort(feedbackList, new Comparator<Feedback>() {
            @Override
            public int compare(Feedback feedback1, Feedback feedback2) {
                // Sắp xếp từ cũ nhất đến mới nhất
                return Long.compare(Long.parseLong(feedback1.getTimestamp()), Long.parseLong(feedback2.getTimestamp()));
            }
        });

        // Thông báo cho Adapter rằng dữ liệu đã thay đổi
        feedbackAdapter.notifyDataSetChanged();
    }
}