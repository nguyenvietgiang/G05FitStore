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
        Glide.with(context).load(product.getImage()).apply(RequestOptions.circleCropTransform()).into(holder.imageView);
        holder.caption.setText(product.getName());
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class HomeAdapterViewHolder extends RecyclerView.ViewHolder {

        TextView caption;
        ImageView imageView;
        public HomeAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            caption = itemView.findViewById(R.id.caption_tv);
            imageView = itemView.findViewById(R.id.image_view);
        }
    }
}
