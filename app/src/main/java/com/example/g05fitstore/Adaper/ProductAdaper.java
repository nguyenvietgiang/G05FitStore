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
import com.example.g05fitstore.Models.Product;
import com.example.g05fitstore.R;

import java.util.ArrayList;

public class ProductAdaper extends RecyclerView.Adapter<ProductAdaper.ProductViewHolder>{
    ArrayList<Product> productArrayList;
    Context context;

    public ProductAdaper(ArrayList<Product> productArrayList, Context context) {
        this.productArrayList = productArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_item, parent,false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Glide.with(context).load(productArrayList.get(position).getImage()).into(holder.imageView);
        holder.textName.setText(productArrayList.get(position).getName());
        holder.textDis.setText(productArrayList.get(position).getDiscription());
    }

    @Override
    public int getItemCount() {
        if(productArrayList == null){
            return 0;
        }
        else {
            return productArrayList.size();
        }
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textName;
        TextView textDis;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.product_image);
            textName = itemView.findViewById(R.id.product_name);
            textDis = itemView.findViewById(R.id.product_discription);
        }
    }
}
