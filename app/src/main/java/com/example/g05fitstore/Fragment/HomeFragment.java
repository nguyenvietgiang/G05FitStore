package com.example.g05fitstore.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.g05fitstore.Adaper.HomeAdapter;
import com.example.g05fitstore.Adaper.ProductAdaper;
import com.example.g05fitstore.Models.Product;
import com.example.g05fitstore.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class HomeFragment extends Fragment {

    RecyclerView recyclerView;
    HomeAdapter homeAdapter;
    ArrayList<Product> productArrayList;
    EditText edtSearchName;
    ImageButton searchBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initUi(view);
        getListProduct("");

        searchBtn.setOnClickListener(v -> {
            searchProduct();
        });


        return view;
    }

    private void initUi(View view) {
        edtSearchName = view.findViewById(R.id.search_username_input);
        searchBtn = view.findViewById(R.id.search_user_btn);

        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        productArrayList = new ArrayList<>();
        homeAdapter = new HomeAdapter(getContext(), productArrayList);
        recyclerView.setAdapter(homeAdapter);
    }

    private void searchProduct() {
        String strKey = edtSearchName.getText().toString().trim();
        if (productArrayList != null) {
            productArrayList.clear();
        } else {
            productArrayList = new ArrayList<>();
        }
        getListProduct(strKey);
//        GlobalFunction.hideSoftKeyboard(getActivity());
    }

    // Hiện danh sách sản phẩm lên recycleview
    private void getListProduct(String key) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productRef = database.getReference().child("list_product");

        productRef.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product product = snapshot.getValue(Product.class);
//                if (product != null) {
//                    productArrayList.add(product);
//                    homeAdapter.notifyDataSetChanged();
//                }
                if (product == null || productArrayList == null || homeAdapter == null) {
                    return;
                }
                if (key.isEmpty()) {
                    productArrayList.add(0, product);
                } else {
                    if (product.getName().toLowerCase().trim()
                            .contains(key.toLowerCase().trim())) {
                        productArrayList.add(0, product);
                    }
                }
                homeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product product = snapshot.getValue(Product.class);
                if(product == null || productArrayList == null || productArrayList.isEmpty()) {
                    return;
                }

                for (int i = 0; i < productArrayList.size(); i++){
                    if(product.getId() == productArrayList.get(i).getId()) {
                        productArrayList.set(i, product);
                    }
                }
                homeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Product product = snapshot.getValue(Product.class);
                if(product == null || productArrayList == null || productArrayList.isEmpty()) {
                    return;
                }
                for (int i = 0; i < productArrayList.size(); i++){
                    if(product.getId() == productArrayList.get(i).getId()) {
                        productArrayList.remove(productArrayList.get(i));
                        break;
                    }
                }
                homeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}