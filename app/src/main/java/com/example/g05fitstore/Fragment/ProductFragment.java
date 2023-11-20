package com.example.g05fitstore.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.g05fitstore.Adaper.ProductAdaper;
import com.example.g05fitstore.Models.Product;
import com.example.g05fitstore.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ProductFragment extends Fragment {
    RecyclerView recyclerView;
    private ProductAdaper mProductAdaper;
    private List<Product> mListProducts;
    private EditText etId, etNameAdd, etDescAdd,etpriceAdd,etpriceUpdate, etNameUpdate , etDescUpdate;
    private Button btnAddProductDialog, btnAddProduct, btnCloseAdd, btnUpdateProduct, btnCloseUpdate;
    private ImageView imageView;

    ActivityResultLauncher<Intent> imagePickLauncher;
    Uri selectedImageUri;
    final private StorageReference storageReference = FirebaseStorage
            .getInstance()
            .getReference("uploads");
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        initUi(view);
        getListProduct();

        imagePickLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK){
                        Intent data = result.getData();
                        if (data!=null && data.getData()!=null){
                            selectedImageUri = data.getData();
                            Glide.with(getContext()).load(selectedImageUri).apply(RequestOptions.circleCropTransform()).into(imageView);
                        }
                    } else {
                        Toast.makeText(getContext(), "No Image Selected", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        btnAddProductDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openAddProductDialog();
            }
        });

        return view;
    }

    // Hàm khởi tạo
    private void initUi(View view) {
        RecyclerView rvProduct = view.findViewById(R.id.rv_product);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvProduct.setLayoutManager(linearLayoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        rvProduct.addItemDecoration(dividerItemDecoration);

        mListProducts = new ArrayList<>();
        mProductAdaper = new ProductAdaper(getContext(), mListProducts, new ProductAdaper.IClickListener() {
            @Override
            public void onClickUpdateItem(Product product) {
                openUpdateProductDialog(product);
            }

            @Override
            public void onCLickDeleteItem(Product product) {
                openDeleteProductAlert(product);
            }
        });
        btnAddProductDialog = view.findViewById(R.id.btn_addProductDialog);

        rvProduct.setAdapter(mProductAdaper);
    }


    // Mở dialog thêm sản phẩm mới
    private void openAddProductDialog() {
        final Dialog dialog = new Dialog(getContext());
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
        etpriceAdd = dialog.findViewById(R.id.et_priceAdd);
        btnAddProduct = dialog.findViewById(R.id.btn_addProduct);
        btnCloseAdd = dialog.findViewById(R.id.btn_closeAdd);
        imageView = dialog.findViewById(R.id.civ_ProductImage);



        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPicker = new Intent();
                photoPicker.setAction(Intent.ACTION_GET_CONTENT);
                photoPicker.setType("image/*");
                imagePickLauncher.launch(photoPicker);
            }
        });

        // Chức năng thêm sản phẩm trong nút dialog (Thêm lên firebase)
        btnAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etNameAdd.getText().toString().trim();
                String desc = etDescAdd.getText().toString().trim();
                String price = etpriceAdd.getText().toString().trim();

                if (name.isEmpty() || desc.isEmpty() || price.isEmpty() || selectedImageUri == null) {
                    // Hiển thị thông báo nếu các trường hoặc hình ảnh không được điền đầy đủ
                    Toast.makeText(getContext(), "Vui lòng điền đầy đủ thông tin và chọn hình ảnh.", Toast.LENGTH_SHORT).show();
                } else if (!TextUtils.isDigitsOnly(price)) {
                    // Hiển thị thông báo nếu giá không hợp lệ (không phải là số)
                    Toast.makeText(getContext(), "Giá nhập vào phải là số.", Toast.LENGTH_SHORT).show();
                } else {
                    // Tiếp tục thực hiện các thao tác khi các trường thỏa mãn yêu cầu
                    final StorageReference imageReference = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(selectedImageUri));
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference productRef = database.getReference().child("list_product");
                    imageReference.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String id = productRef.push().getKey();
                                    String userId = FirebaseAuth.getInstance().getUid();
                                    String name = etNameAdd.getText().toString().trim();
                                    String image = uri.toString();
                                    String desc = etDescAdd.getText().toString().trim();
                                    Integer price = Integer.parseInt(etpriceAdd.getText().toString().trim());
                                    Product product = new Product(id, userId, name, image, desc,price);

                                    String pathObject = String.valueOf(product.getId());
                                    productRef.child(pathObject).setValue(product, new DatabaseReference.CompletionListener() {
                                        @Override
                                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                            if (error != null) {
                                                // Hiển thị thông báo nếu thêm sản phẩm thất bại
                                                Toast.makeText(getContext(), "Thêm sản phẩm thất bại!", Toast.LENGTH_SHORT).show();
                                            } else {
                                                // Hiển thị thông báo nếu thêm sản phẩm thành công
                                                Toast.makeText(getContext(), "Thêm thành công!", Toast.LENGTH_SHORT).show();
                                            }
                                            dialog.dismiss();
                                        }
                                    });
                                }
                            });
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Hiển thị thông báo nếu việc tải lên hình ảnh thất bại
                            Toast.makeText(getContext(), "Tải lên hình ảnh thất bại!", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
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
        final Dialog dialog = new Dialog(getContext());
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
        etpriceUpdate = dialog.findViewById(R.id.et_priceUpdate);
        btnUpdateProduct = dialog.findViewById(R.id.btn_UpdateProduct);
        btnCloseUpdate = dialog.findViewById(R.id.btn_closeUpdate);

        etNameUpdate.setText(product.getName());
        etDescUpdate.setText(product.getDesc());
        etpriceUpdate.setText(product.getPrice()+""); // thêm +"" vì là int
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
        new AlertDialog.Builder(getContext())
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
                                Toast.makeText(getContext(), "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
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
        Integer newPrice =  Integer.parseInt(etpriceUpdate.getText().toString().trim());
        product.setName(newName);
        product.setDesc(newDesc);
        product.setPrice(newPrice);
        productRef.child(String.valueOf(product.getId())).updateChildren(product.toMap(), new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                Toast.makeText(getContext(), "Cập nhật thành công !", Toast.LENGTH_SHORT).show();
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
                    if (product.getUserId().contains(FirebaseAuth.getInstance().getUid())){
                        mListProducts.add(product);
                        mProductAdaper.notifyDataSetChanged();
                    }
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

    private String getFileExtension(Uri fileUri){
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(fileUri));
    }
}