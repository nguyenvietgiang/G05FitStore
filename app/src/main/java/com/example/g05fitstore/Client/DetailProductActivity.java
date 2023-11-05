package com.example.g05fitstore.Client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.g05fitstore.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailProductActivity extends AppCompatActivity {

    TextView tvProductName ,tvDetailDesc, tvUser, tvPrice;
    ImageView ivDetail;

    CircleImageView civBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);

        tvProductName = findViewById(R.id.product_name);
        tvUser = findViewById(R.id.username);
        tvDetailDesc = findViewById(R.id.description);
        tvPrice = findViewById(R.id.price);
        ivDetail = findViewById(R.id.image_view);
        civBack = findViewById(R.id.civ_back);

        tvProductName.setText(getIntent().getStringExtra("name"));
        tvUser.setText(tvUser.getText() + getIntent().getStringExtra("username"));
        tvDetailDesc.setText(getIntent().getStringExtra("description"));
//        tvPrice.setText(getIntent().getStringExtra("price")+ " đ");
        tvPrice.setText("20000" + " đ");
        Glide.with(this).load(getIntent().getStringExtra("image")).into(ivDetail);

        // Nut tro lai
        civBack.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}