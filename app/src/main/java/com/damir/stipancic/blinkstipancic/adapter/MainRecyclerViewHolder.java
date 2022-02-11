package com.damir.stipancic.blinkstipancic.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.damir.stipancic.blinkstipancic.R;
import com.damir.stipancic.blinkstipancic.contract.Contract;
import com.damir.stipancic.blinkstipancic.contract.MainContract;

public class MainRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, MainContract.View.itemView{

    private final TextView tvDocument;
    private final MainRecyclerAdapter.OnDocumentClick mOnDocumentClick;

    public MainRecyclerViewHolder(@NonNull View itemView, MainRecyclerAdapter.OnDocumentClick onDocumentClick) {
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
    public void setTitle(int title) {
        tvDocument.setText(String.valueOf(title));
    }
}
