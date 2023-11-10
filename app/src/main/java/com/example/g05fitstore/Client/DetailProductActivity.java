package com.example.g05fitstore.Client;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.g05fitstore.Models.Transaction;
import com.example.g05fitstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class DetailProductActivity extends AppCompatActivity {

    TextView tvProductName ,tvDetailDesc, tvUser, tvPrice;
    ImageView ivDetail;
    Button btnBuy;
    CircleImageView civBack;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cartItem");
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
        btnBuy = findViewById(R.id.btn_buy);
        tvProductName.setText(getIntent().getStringExtra("name"));
        tvUser.setText(tvUser.getText() + getIntent().getStringExtra("username"));
        tvDetailDesc.setText(getIntent().getStringExtra("description"));
        tvPrice.setText(getIntent().getStringExtra("price")+ " đ");
        Glide.with(this).load(getIntent().getStringExtra("image")).into(ivDetail);

        // nút trở lại
        civBack.setOnClickListener(v -> {
            onBackPressed();
        });
        // ấn nút thêm vào giỏ gọi hộp thoại xác nhận
        btnBuy.setOnClickListener(v -> {
            showConfirmationDialog();
        });

    }
       // hiện hộp thoại xác nhận
    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Xác nhận");
        builder.setMessage("Bạn có chắc chắn muốn thêm vào giỏ hàng không?");

        builder.setPositiveButton("Có", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addToCart();
            }
        });

        builder.setNegativeButton("Không", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Không thực hiện gì cả nếu người dùng chọn "Không"
                dialog.dismiss();
            }
        });

        builder.show();
    }
     // sự kiện thêm vào giỏ hàng
    private void addToCart() {
        String transactionId = databaseReference.push().getKey();
        String buyerId = FirebaseAuth.getInstance().getUid();
        String sellerId = getIntent().getStringExtra("sellerid");
        String productId = getIntent().getStringExtra("productId");
        String totalValue = getIntent().getStringExtra("price");
        Transaction transaction = new Transaction(transactionId, buyerId, sellerId, productId, totalValue);
        databaseReference.child(transactionId).setValue(transaction).addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Thêm sản phẩm thất bại", Toast.LENGTH_SHORT).show();
            }
        });
    }
}