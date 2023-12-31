package com.example.g05fitstore.Adaper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.g05fitstore.Models.Product;
import com.example.g05fitstore.R;

import java.util.List;


public class ProductAdaper extends RecyclerView.Adapter<ProductAdaper.ProductViewHolder>{

    Context context;
    private List<Product> mListProduct;
    private IClickListener mIClickListener;
    public interface IClickListener {
        void onClickUpdateItem(Product product);
        void onCLickDeleteItem(Product product);
    }

    public ProductAdaper(Context context, List<Product> mListProduct, IClickListener listener) {
        this.context = context;
        this.mListProduct = mListProduct;
        this.mIClickListener = listener;
    }

    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = mListProduct.get(position);
        if(product ==  null) {
            return;
        }
        Glide.with(context).load(product.getImage()).apply(RequestOptions.circleCropTransform()).into(holder.imageView);
        holder.tvName.setText(product.getName());
        holder.tvDesc.setText(product.getDesc());

        holder.IbtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIClickListener.onClickUpdateItem(product);
            }
        });

        holder.IbtnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mIClickListener.onCLickDeleteItem(product);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mListProduct != null) {
            return mListProduct.size();
        }
        return 0;
    }

    public class ProductViewHolder extends RecyclerView.ViewHolder {

        private TextView tvName;
        private TextView tvDesc;
        private ImageButton IbtnUpdate, IbtnDelete;
        private ImageView imageView;
        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            IbtnUpdate = (ImageButton) itemView.findViewById(R.id.ibtn_Update);
            IbtnDelete = (ImageButton) itemView.findViewById(R.id.ibtn_Delete);
            imageView = itemView.findViewById(R.id.civAvatarProfile);
        }
    }

}
