package com.example.g05fitstore.Fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g05fitstore.Client.ICartSum;
import com.google.firebase.auth.FirebaseAuth;
import com.example.g05fitstore.Adaper.ShoppingCartAdapter;
import com.example.g05fitstore.Models.Transaction;
import com.example.g05fitstore.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment implements ICartSum {

    private RecyclerView recyclerView;
    private ShoppingCartAdapter shoppingCartAdapter;
    private List<Transaction> transactionList;
    private TextView totalAmountTextView;

    public CartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        totalAmountTextView = view.findViewById(R.id.total_amount_text_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        transactionList = new ArrayList<>();
        shoppingCartAdapter = new ShoppingCartAdapter(transactionList, recyclerView, getContext(), this::cartSum);
        recyclerView.setAdapter(shoppingCartAdapter);
        String buyerId = FirebaseAuth.getInstance().getUid();
        if (buyerId != null) {
            loadTransactionsFromFirebase(buyerId);
        }
        return view;
    }

    private void loadTransactionsFromFirebase(String buyerId) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("cartItem");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                transactionList.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Transaction transaction = snapshot.getValue(Transaction.class);

                    // Only add transactions with matching buyerId
                    if (transaction != null && buyerId.equals(transaction.getBuyerId())) {
                        transactionList.add(transaction);
                    }
                }

                shoppingCartAdapter.notifyDataSetChanged();
//                // Kiểm tra nếu transactionList đã được cập nhật, sau đó tính tổng
//                if (!transactionList.isEmpty()) {
//                    double totalAmount = shoppingCartAdapter.getTotalAmount();
//                    totalAmountTextView.setText(String.valueOf(totalAmount));
//                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Xử lý lỗi
            }
        });
    }

    @Override
    public void cartSum() {
        double totalAmount = shoppingCartAdapter.getTotalAmount();
        totalAmountTextView.setText(String.valueOf(totalAmount));
    }
}