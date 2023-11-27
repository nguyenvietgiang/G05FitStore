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

public class LoginActivity extends AppCompatActivity {
    EditText editTextEmail, editTextPassword;
    Button btnLogin;
    FirebaseAuth mAuth;
    ProgressBar progressBar;
    TextView textView;

    int loginAttempts = 0; //biến đếm số lần đăng nhập sai

    @Override
    public void onStart() {
        super.onStart();
        // ko cần đăng nhập lại.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivities(new Intent[]{intent});
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        editTextEmail = findViewById(R.id.email);
        editTextPassword =findViewById(R.id.password);
        btnLogin =findViewById(R.id.loginButton);
        progressBar = findViewById(R.id.progrebar);
        textView =findViewById(R.id.signupText);

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivities(new Intent[]{intent});
                finish();;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                String email, password;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(LoginActivity.this, "Hãy nhập Email", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(password)){
                    Toast.makeText(LoginActivity.this, "Hãy nhập Password", Toast.LENGTH_SHORT).show();
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    loginAttempts = 0; // Reset the counter on successful login
                                    Toast.makeText(LoginActivity.this, "Đăng nhập thành công",
                                            Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    startActivities(new Intent[]{intent});
                                    finish();
                                } else {
                                    // If sign-in fails, display a message to the user.
                                    loginAttempts++;
                                    if (loginAttempts == 5) {
                                        Toast.makeText(LoginActivity.this, "Sai tên email hoặc mật khẩu. Đăng nhập thất bại 5 lần. Hãy đăng ký!",
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                                        startActivities(new Intent[]{intent});
                                        finish();
                                    } else {
                                        Toast.makeText(LoginActivity.this, "Sai tên email hoặc mật khẩu",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });
            }

        });
    }
}