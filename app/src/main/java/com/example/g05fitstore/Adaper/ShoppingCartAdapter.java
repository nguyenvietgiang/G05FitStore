package com.example.g05fitstore.Adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.g05fitstore.Models.Product;
import com.example.g05fitstore.Models.Transaction;
import com.example.g05fitstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCartAdapter extends RecyclerView.Adapter<ShoppingCartAdapter.ViewHolder> {

    private List<Transaction> transactionList;
    private double totalAmount = 0.0;
    private Map<String, Product> productMap;
    private RecyclerView recyclerView;
    private Context context;

    public ShoppingCartAdapter(List<Transaction> transactionList, RecyclerView recyclerView,Context context) {
        this.transactionList = transactionList;
        this.productMap = new HashMap<>();
        this.recyclerView = recyclerView;
        this.context = context;
    }

    public double calculateTotal() {
        totalAmount = 0.0;
        for (Transaction transaction : transactionList) {
            Product product = productMap.get(transaction.getProductId());
            if (product != null) {
                totalAmount += product.getPrice();
            }
        }
        return totalAmount;
    }

    public double getTotalAmount() {
        return calculateTotal();
    }

    public void removeTransaction(int position) {
        Transaction removedTransaction = transactionList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());

        // Xóa transaction khỏi Firebase
        String transactionId = removedTransaction.getTransactionId();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cartItem").child(transactionId);
        databaseReference.removeValue();

        // Cập nhật tổng số tiền
        updateTotalAmount();
    }

    private void updateTotalAmount() {
        totalAmount = calculateTotal();
        ((TextView) recyclerView.getRootView().findViewById(R.id.total_amount_text_view))
                .setText(String.valueOf(totalAmount));
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart_product, parent, false);
        return new ViewHolder(view, recyclerView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        loadProductInfo(transaction.getProductId(), holder);
        holder.transaction = transaction;
    }

    @Override
    public int getItemCount() {
        return transactionList.size();
    }

    private void loadProductInfo(String productId, ViewHolder holder) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("list_product").child(productId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Product product = dataSnapshot.getValue(Product.class);
                    if (product != null) {
                        holder.ProductNameTextView.setText(product.getName());
                        holder.ProductPriceTextView.setText(String.valueOf(product.getPrice()));
                        Glide.with(context).load(product.getImage()).apply(RequestOptions.circleCropTransform()).into(holder.imageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý khi có lỗi
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView ProductNameTextView;
        TextView ProductPriceTextView;
        ImageView imageView;
        Button deleteButton;
        Transaction transaction;
        RecyclerView recyclerView;

        public ViewHolder(@NonNull View itemView, RecyclerView recyclerView) {
            super(itemView);
            ProductNameTextView = itemView.findViewById(R.id.caption_tv);
            ProductPriceTextView = itemView.findViewById(R.id.price);
            imageView = itemView.findViewById(R.id.image_view);
            deleteButton = itemView.findViewById(R.id.delete_button);
            this.recyclerView = recyclerView;

            deleteButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    ((ShoppingCartAdapter) recyclerView.getAdapter()).removeTransaction(position);
                }
            });
        }
    }
}



