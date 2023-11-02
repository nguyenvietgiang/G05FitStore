package com.example.g05fitstore.Client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.g05fitstore.R;

public class DetailProductActivity extends AppCompatActivity {

    TextView name;
    TextView userName;
    TextView description;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        name = findViewById(R.id.product_name);
        userName = findViewById(R.id.username);
        description = findViewById(R.id.description);
        imageView = findViewById(R.id.image_view);

        name.setText(getIntent().getStringExtra("name"));
        userName.setText(getIntent().getStringExtra("username"));
        description.setText(getIntent().getStringExtra("description"));
        Glide.with(this).load(getIntent().getStringExtra("image")).into(imageView);

        // Nut tro lai
        name.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}