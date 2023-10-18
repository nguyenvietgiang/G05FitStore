package com.example.g05fitstore.Client;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.g05fitstore.MainActivity;
import com.example.g05fitstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {
    EditText editTextUsername, editTextEmail, editTextPassword;
    Button btnReg;
    FirebaseAuth mAuth;

    DatabaseReference databaseReference;
    ProgressBar progressBar;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        addControls();
        addEvents();


        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivities(new Intent[]{intent});
                finish();;
            }
        });

    }

    private void addControls() {
        mAuth = FirebaseAuth.getInstance();
        editTextUsername = findViewById(R.id.username);
        editTextEmail =findViewById(R.id.email);
        editTextPassword =findViewById(R.id.password);
        btnReg =findViewById(R.id.registerBtn);
        progressBar = findViewById(R.id.progrebar);
        textView =findViewById(R.id.loginText);
    }

    private void addEvents(){
        onClickBtnRegister();
    }
    private void register(String username, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userID = firebaseUser.getUid();

                            databaseReference = FirebaseDatabase.getInstance().getReference("Users").child(userID);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("userID",userID);
                            hashMap.put("userName", username);
                            hashMap.put("nickName", username);
                            hashMap.put("avatar", "default");
                            hashMap.put("activeStatus","offline");
                            hashMap.put("keySearch",username.toLowerCase());
                            hashMap.put("studentCode","Chưa cập nhập");
                            hashMap.put("className","Chưa cập nhập");
                            hashMap.put("address","Chưa cập nhập");
                            hashMap.put("specialized","Chưa cập nhập");


                            //ghi dữ liệu user
                            databaseReference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
//                                        Intent intent  = new Intent(RegisterActivity.this, MainActivity.class);
//                                        //khi hoạt động bắt đầu, tất cả các hoạt động khác hiện tại sẽ bị hủy bỏ
//                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });


                            // Sign in success, update UI with the signed-in user's information
                            // FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this, "Đăng ký thành công.",
                                    Toast.LENGTH_SHORT).show();

//                            Intent intent = new Intent(RegisterActivity.this, MyProfileActivity.class);
//                            intent.putExtra("KEYUSER",userID );
//                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(RegisterActivity.this, "Đăng ký thất bại.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void onClickBtnRegister() {
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password, username;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                username = String.valueOf(editTextUsername.getText());
                if (TextUtils.isEmpty(email)){
                    Toast.makeText(RegisterActivity.this, "Hãy nhập Email", Toast.LENGTH_SHORT).show();
                } else if (TextUtils.isEmpty(password)){
                    Toast.makeText(RegisterActivity.this, "Hãy nhập Password", Toast.LENGTH_SHORT).show();
                }else {
                    register(username, email, password);

                }


            }
        });
    }
}