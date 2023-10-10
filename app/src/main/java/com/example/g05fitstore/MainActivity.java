package com.example.g05fitstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.g05fitstore.Client.LoginActivity;
import com.example.g05fitstore.Fragment.adviseFragment;
import com.example.g05fitstore.Models.Category;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;
    Button button;
    TextView textView;
    FirebaseUser user;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();
       // button = findViewById(R.id.logoutBtn);
        textView = findViewById(R.id.username);
        bottomNavigationView = findViewById(R.id.bottom_nav_view);
        user = auth.getCurrentUser();
        if (user== null)
        {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivities(new Intent[]{intent});
            finish();
        }else
        {
            textView.setText(user.getEmail());
            Fragment fragment = new adviseFragment();
            loadFragment(fragment);
        }
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                FirebaseAuth.getInstance().signOut();
//                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
//                startActivities(new Intent[]{intent});
//                finish();
//            }
//        });
    }
    private void addEvents() {
        eventBottomNavOnClick();
    }

    public void loadFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_main, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void eventBottomNavOnClick() {
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Fragment fragment = null;

                if (menuItem.getItemId() == R.id.item_supp) {
                    fragment = new adviseFragment();
                    loadFragment(fragment);
                    return true;
                } else {
                    return false;
                }
            }
        });

    }
}