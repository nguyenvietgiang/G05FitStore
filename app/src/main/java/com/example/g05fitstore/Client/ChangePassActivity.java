package com.example.g05fitstore.Client;

import static java.security.AccessController.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.g05fitstore.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChangePassActivity extends AppCompatActivity {

    TextView back;
    Button changePass;
    EditText currentPass, newPass, confirmPass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        changePass = findViewById(R.id.changeBtn);
        back = findViewById(R.id.backtv);
       // currentPass = findViewById(R.id.currentPass);
        newPass = findViewById(R.id.newPass);
       // confirmPass = findViewById(R.id.confirmPass);

        changePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog();;
            }
        });
        // trở về trang trước
        back.setOnClickListener(v -> onBackPressed());
    }

    private void showConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassActivity.this);
        builder.setTitle("Xác nhận thay đổi mật khẩu");
        builder.setMessage("Bạn có chắc chắn muốn thay đổi mật khẩu không?");
        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                changePassword();
            }
        });
        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    // kiểm tra mật khẩu cũ
//    private void showConfirmationDialog() {
//        AlertDialog.Builder builder = new AlertDialog.Builder(ChangePassActivity.this);
//        builder.setTitle("Xác nhận thay đổi mật khẩu");
//        builder.setMessage("Bạn có chắc chắn muốn thay đổi mật khẩu không?");
//        builder.setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                // Lấy mật khẩu hiện tại từ người dùng
//                String currentPassword = currentPass.getText().toString();
//
//                // Lấy đối tượng FirebaseUser
//                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//                // Tạo một đối tượng AuthCredential để xác thực lại người dùng
//                AuthCredential credential = EmailAuthProvider.getCredential(user.getEmail(), currentPassword);
//
//                // Reauthenticate người dùng với mật khẩu hiện tại
//                user.reauthenticate(credential)
//                        .addOnCompleteListener(new OnCompleteListener<Void>() {
//                            @Override
//                            public void onComplete(@NonNull Task<Void> task) {
//                                if (task.isSuccessful()) {
//                                    // Xác thực thành công, tiến hành đổi mật khẩu
//                                    changePassword();
//                                } else {
//                                    // Xác thực không thành công, hiển thị thông báo lỗi
//                                    Toast.makeText(ChangePassActivity.this, "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
//                                }
//                            }
//                        });
//            }
//        });
//        builder.setNegativeButton("Hủy bỏ", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.show();
//    }

    private void changePassword(){
        String newPassword = newPass.getText().toString();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            return;
        }
        user.updatePassword(newPassword).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Toast.makeText(ChangePassActivity.this, "Mật khẩu đã được thay đổi", Toast.LENGTH_SHORT).show();
            }
        });
    }
}