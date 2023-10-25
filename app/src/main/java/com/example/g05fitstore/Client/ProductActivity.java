package com.example.g05fitstore.Client;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.g05fitstore.Adaper.ProductAdaper;
import com.example.g05fitstore.Models.Product;
import com.example.g05fitstore.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class ProductActivity extends AppCompatActivity {
    private ProductAdaper mProductAdaper;
    private List<Product> mListProducts;
    private EditText etId, etNameAdd, etDescAdd, etNameUpdate, etDescUpdate;
    private Button btnAddProductDialog, btnAddProduct, btnCloseAdd, btnUpdateProduct, btnCloseUpdate;

    public ProductActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        initUi();
        getListProduct();

        btnAddProductDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddProductDialog();
            }
        });

    }

    // Hàm khởi tạo
    private void initUi() {
        RecyclerView rvProduct = findViewById(R.id.rv_product);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        rvProduct.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        rvProduct.addItemDecoration(dividerItemDecoration);

        mListProducts = new ArrayList<>();
        mProductAdaper = new ProductAdaper(mListProducts, new ProductAdaper.IClickListener() {
            @Override
            public void onClickUpdateItem(Product product) {
                openUpdateProductDialog(product);
            }

            @Override
            public void onCLickDeleteItem(Product product) {
                openDeleteProductAlert(product);
            }
        });
        btnAddProductDialog = findViewById(R.id.btn_addProductDialog);

        rvProduct.setAdapter(mProductAdaper);
    }


    // Mở dialog thêm sản phẩm mới
    private void openAddProductDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_add_product);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);
        etNameAdd = dialog.findViewById(R.id.et_nameAdd);
        etDescAdd = dialog.findViewById(R.id.et_descAdd);
        btnAddProduct = dialog.findViewById(R.id.btn_addProduct);
        btnCloseAdd = dialog.findViewById(R.id.btn_closeAdd);

        // Chức năng thêm sản phẩm trong nút dialog (Thêm lên firebase)
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference productRef = database.getReference().child("list_product");

                String id = productRef.push().getKey();
                String name = etNameAdd.getText().toString().trim();
                String image = "default";
                String desc = etDescAdd.getText().toString().trim();
                Product product = new Product(id, name, image, desc);

                String pathObject = String.valueOf(product.getId());
                productRef.child(pathObject).setValue(product, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                        Toast.makeText(ProductActivity.this, "Thêm thành công !", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
            }
        });
        btnCloseAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    private void openUpdateProductDialog(Product product) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_update_product);

        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        WindowManager.LayoutParams windowAttributes = window.getAttributes();
        windowAttributes.gravity = Gravity.CENTER;
        window.setAttributes(windowAttributes);
        dialog.setCancelable(true);

        etNameUpdate = dialog.findViewById(R.id.et_nameUpdate);
        etDescUpdate = dialog.findViewById(R.id.et_descUpdate);
        btnUpdateProduct = dialog.findViewById(R.id.btn_UpdateProduct);
        btnCloseUpdate = dialog.findViewById(R.id.btn_closeUpdate);

        etNameUpdate.setText(product.getName());
        etDescUpdate.setText(product.getDesc());

        btnCloseUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        btnUpdateProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickUpdateProduct(product, dialog);
            }
        });

        dialog.show();
    }



    private void openDeleteProductAlert(Product product) {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.app_name))
                .setMessage("Bạn có chắc muốn xóa sản phẩm này không ?")
                .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference productRef = database.getReference().child("list_product");

                        productRef.child(String.valueOf(product.getId())).removeValue(new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                Toast.makeText(ProductActivity.this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .setPositiveButton("Hủy", null)
                .show();
    }

    private void onClickUpdateProduct(Product product, Dialog dialog) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productRef = database.getReference().child("list_product");

        String newName = etNameUpdate.getText().toString().trim();
        String newDesc = etDescUpdate.getText().toString().trim();
        product.setName(newName);
        product.setDesc(newDesc);
        productRef.child(String.valueOf(product.getId())).updateChildren(product.toMap(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(ProductActivity.this, "Cập nhật thành công !", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
    }

    // Hiện danh sách sản phẩm lên recycleview
    private void getListProduct() {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference productRef = database.getReference().child("list_product");

        productRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product product = snapshot.getValue(Product.class);
                if (product != null) {
                    mListProducts.add(product);
                    mProductAdaper.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Product product = snapshot.getValue(Product.class);
                if(product == null || mListProducts == null || mListProducts.isEmpty()) {
                    return;
                }

                for (int i = 0; i < mListProducts.size(); i++){
                    if(product.getId() == mListProducts.get(i).getId()) {
                        mListProducts.set(i, product);
                    }
                }
                mProductAdaper.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                Product product = snapshot.getValue(Product.class);
                if(product == null || mListProducts == null || mListProducts.isEmpty()) {
                    return;
                }
                for (int i = 0; i < mListProducts.size(); i++){
                    if(product.getId() == mListProducts.get(i).getId()) {
                        mListProducts.remove(mListProducts.get(i));
                        break;
                    }
                }
                mProductAdaper.notifyDataSetChanged();
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
