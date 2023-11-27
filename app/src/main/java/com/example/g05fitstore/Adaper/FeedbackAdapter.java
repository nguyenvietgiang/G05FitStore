package com.example.g05fitstore.Adaper;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.g05fitstore.Models.Feedback;
import com.example.g05fitstore.R;

import java.util.List;

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.FeedbackViewHolder> {
    private Context context;
    private List<Feedback> feedbackList;

    public FeedbackAdapter(Context context, List<Feedback> feedbackList) {
        this.context = context;
        this.feedbackList = feedbackList;
    }

    @NonNull
    @Override
    public FeedbackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_feedback, parent, false);
        return new FeedbackViewHolder(view);
    }
    // xóa item
    public interface OnDeleteClickListener {
        void onDeleteClick(int position);
    }

    private OnDeleteClickListener onDeleteClickListener;

    public void setOnDeleteClickListener(OnDeleteClickListener listener) {
        this.onDeleteClickListener = listener;
    }


    @Override
    public void onBindViewHolder(@NonNull FeedbackViewHolder holder, final int position) {
        Feedback feedback = feedbackList.get(position);

        holder.contentTextView.setText(feedback.getContent());
        holder.numTextView.setText(String.valueOf(feedback.getNum()));
        holder.timestampTextView.setText(feedback.getTimestamp());

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                if (onDeleteClickListener != null && clickedPosition != RecyclerView.NO_POSITION) {
                    onDeleteClickListener.onDeleteClick(clickedPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return feedbackList.size();
    }

    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {
        TextView contentTextView;
        TextView numTextView;
        TextView timestampTextView;
        Button btnDelete;
        public FeedbackViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTextView = itemView.findViewById(R.id.contentTextView);
            numTextView = itemView.findViewById(R.id.numTextView);
            timestampTextView = itemView.findViewById(R.id.timestampTextView);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
