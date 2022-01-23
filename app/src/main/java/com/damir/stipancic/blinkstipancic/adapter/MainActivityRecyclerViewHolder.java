package com.damir.stipancic.blinkstipancic.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.damir.stipancic.blinkstipancic.R;
import com.damir.stipancic.blinkstipancic.contract.Contract;

public class MainActivityRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, Contract.View.MainActivityView.itemView{

    private final TextView tvDocument;
    private final MainActivityRecyclerAdapter.OnDocumentClick mOnDocumentClick;

    public MainActivityRecyclerViewHolder(@NonNull View itemView, MainActivityRecyclerAdapter.OnDocumentClick onDocumentClick) {
        super(itemView);
        this.mOnDocumentClick = onDocumentClick;
        itemView.setOnClickListener(this);
        tvDocument = itemView.findViewById(R.id.tvDocument);
    }

    @Override
    public void onClick(View view) {
        mOnDocumentClick.onClick(view, getAdapterPosition());
    }

    @Override
    public void setTitle(String title) {
        tvDocument.setText(title);
    }
}
