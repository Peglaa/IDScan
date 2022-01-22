package com.damir.stipancic.blinkstipancic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.damir.stipancic.blinkstipancic.R;
import com.damir.stipancic.blinkstipancic.presenters.MainActivityPresenter;

public class MainActivityRecyclerAdapter extends RecyclerView.Adapter<MainActivityRecyclerViewHolder> {

    private final MainActivityPresenter mMainActivityPresenter;
    private final OnDocumentClick mListener;

    public MainActivityRecyclerAdapter(MainActivityPresenter presenter, OnDocumentClick listener){
        this.mListener = listener;
        this.mMainActivityPresenter = presenter;
    }

    @NonNull
    @Override
    public MainActivityRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_item, parent, false);
        return new MainActivityRecyclerViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivityRecyclerViewHolder holder, int position) {
        mMainActivityPresenter.onBindArticleData(holder, position);
    }

    @Override
    public int getItemCount() {
        return mMainActivityPresenter.getDocumentCount();
    }

    public interface OnDocumentClick{
        void onClick(View v, int position);
    }
}
