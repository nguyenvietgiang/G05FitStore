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
import com.google.firebase.auth.FirebaseAuth;
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
    EditText edtSearchName, edtMinPrice, edtMaxPrice;
    ImageButton searchBtn;
    boolean isAscendingOrder = true; // Theo dõi trạng thái sắp xếp
    // Được gọi khi Fragment được tạo. bao gồm gọi initUi
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        initUi(view);
        getListProduct("",0,0);
        // tìm theo khoảng giá b1
//        getListProduct("", 0, Integer.MAX_VALUE);
        searchBtn.setOnClickListener(v -> {
            searchProduct();
        });


        return view;
    }
      // Ánh xạ các thành phần giao diện và khởi tạo RecyclerView
    private void initUi(View view) {
        edtSearchName = view.findViewById(R.id.search_username_input);
        searchBtn = view.findViewById(R.id.search_user_btn);
        // tìm kiếm khoảng giá b2
//        edtMinPrice = view.findViewById(R.id.edtMinPrice);
//        edtMaxPrice = view.findViewById(R.id.edtMaxPrice);
        recyclerView = view.findViewById(R.id.recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        productArrayList = new ArrayList<>();
        homeAdapter = new HomeAdapter(getContext(), productArrayList);
        recyclerView.setAdapter(homeAdapter);
    }
//    Lấy từ khóa tìm kiếm từ EditText kèm giá. và gọi getListProduct b3
//   private void searchProduct() {
//       String strKey = edtSearchName.getText().toString().trim();
//       String strMinPrice = edtMinPrice.getText().toString().trim();
//       String strMaxPrice = edtMaxPrice.getText().toString().trim();
//
//       // Kiểm tra nếu giá trị min và max không rỗng
//       if (!strMinPrice.isEmpty() && !strMaxPrice.isEmpty()) {
//           int minPrice = Integer.parseInt(strMinPrice);
//          int maxPrice = Integer.parseInt(strMaxPrice);
//
//           // Nếu minPrice <= maxPrice, thì tiến hành tìm kiếm
//           if (minPrice <= maxPrice) {
//               if (productArrayList != null) {
//                  productArrayList.clear();
//               } else {
//                  productArrayList = new ArrayList<>();
//               }
//               getListProduct(strKey, minPrice, maxPrice);
//          } else {
//               // Hiển thị thông báo hoặc xử lý khi minPrice > maxPrice
//           }
//      } else {
//           // Nếu giá trị min hoặc max rỗng, thì tiến hành tìm kiếm theo tên sản phẩm
//           if (productArrayList != null) {
//               productArrayList.clear();
//           } else {
//               productArrayList = new ArrayList<>();
//           }
//           // Chuyển thêm giá trị minPrice và maxPrice khi gọi getListProduct
//           getListProduct(strKey, 0, Integer.MAX_VALUE); // Bạn có thể thay thế Integer.MAX_VALUE bằng giá trị tối đa mong muốn
//       }
//   }

    // tìm ko kèm giá
    private void searchProduct() {
        String strKey = edtSearchName.getText().toString().trim();
        if (productArrayList != null) {
            productArrayList.clear();
        } else {
            productArrayList = new ArrayList<>();
        }
        getListProduct(strKey,0,0);
//        GlobalFunction.hideSoftKeyboard(getActivity());
    }

    // Chỉnh sửa phương thức getListProduct để thêm tham số minPrice và maxPrice
    private void getListProduct(String key, int minPrice, int maxPrice) {
    // Hiện danh sách sản phẩm lên recycleview
    //    private void getListProduct(String key) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productRef = database.getReference().child("list_product");

        productRef.addChildEventListener(new ChildEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            // tìm kiếm theo tên thôi
           public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
               Product product = snapshot.getValue(Product.class);
//                if (product != null) {
//                    if (product.getUserId().contains(FirebaseAuth.getInstance().getUid())){
//                        productArrayList.add(0, product);
//                        homeAdapter.notifyDataSetChanged();
//                    }
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
             // tìm kèm khoảng giá nữa b4
//            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                Product product = snapshot.getValue(Product.class);
//                if (product == null || productArrayList == null || homeAdapter == null) {
//                    return;
//                }
//
//                // Thêm điều kiện kiểm tra giá sản phẩm nằm trong khoảng minPrice và maxPrice
//                if (product.getPrice() >= minPrice && product.getPrice() <= maxPrice) {
//                    if (key.isEmpty()) {
//                        productArrayList.add(0, product);
//                    } else {
//                        if (product.getName().toLowerCase().trim().contains(key.toLowerCase().trim())) {
//                            productArrayList.add(0, product);
//                        }
//                    }
//                    homeAdapter.notifyDataSetChanged();
//                }
//            }

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