package com.example.g05fitstore.Client;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.g05fitstore.Models.User;
import com.example.g05fitstore.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditProfileActivity extends AppCompatActivity {
    private EditText editTextUserName, editTextNickName, editTextAddress, editTextPhone;
    private Button btnSave,btnBack;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());

        editTextUserName = findViewById(R.id.editTextUserName);
        editTextNickName = findViewById(R.id.editTextNickName);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextPhone = findViewById(R.id.editTextPhone);
        btnSave = findViewById(R.id.btnSave);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> onBackPressed());
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfileChanges();
            }
        });
    }

    private void saveProfileChanges() {
        String userName = editTextUserName.getText().toString().trim();
        String nickName = editTextNickName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        if (!userName.isEmpty() && !nickName.isEmpty() && !address.isEmpty()) {
            mDatabase.child("userName").setValue(userName);
            mDatabase.child("nickName").setValue(nickName);
            mDatabase.child("address").setValue(address);
            mDatabase.child("className").setValue(phone);

            User currentUser = new User();
            currentUser.setUserName(userName);
            currentUser.setNickName(nickName);
            currentUser.setAddress(address);
            currentUser.setClassName(phone);
            showToast("Thông tin đã được cập nhật thành công");
            finish();
        } else {
            showToast("Hãy điền đầy đủ thông tin");
        }
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

}