package com.example.g05fitstore.Adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.g05fitstore.Models.Product;
import com.example.g05fitstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeAdapterViewHolder>{

    Context context;
    List<Product> productList;

    public HomeAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public HomeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_product, parent, false);
        return new HomeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeAdapterViewHolder holder, int position) {
        Product product = productList.get(position);
        Glide.with(context).load(product.getImage()).into(holder.imageView);
        holder.caption.setText(product.getName());
        FirebaseDatabase.getInstance().getReference("Users/"+product.getUserId())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Product product1 = snapshot.getValue(Product.class);
                        String username =product1.getName();
                        holder.username.setText(username);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class HomeAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView caption, username;
        ImageView imageView;
        public HomeAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            caption = itemView.findViewById(R.id.caption_tv);
            imageView = itemView.findViewById(R.id.image_view);
            username = itemView.findViewById(R.id.username);
        }
    }
}
