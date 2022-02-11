package com.damir.stipancic.blinkstipancic.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.damir.stipancic.blinkstipancic.R;
import com.damir.stipancic.blinkstipancic.contract.MainContract;

public class MainRecyclerAdapter extends RecyclerView.Adapter<MainRecyclerViewHolder> {

    private final MainContract.Presenter mMainPresenter;
    private final OnDocumentClick mListener;

    public MainRecyclerAdapter(MainContract.Presenter presenter, OnDocumentClick listener){
        this.mListener = listener;
        this.mMainPresenter = presenter;
    }

    @NonNull
    @Override
    public MainRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_item, parent, false);
        return new MainRecyclerViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MainRecyclerViewHolder holder, int position) {
        mMainPresenter.onBindDocumentData(holder, position);
    }

    @Override
    public int getItemCount() {
        return mMainPresenter.getDocumentCount();
    }

    public interface OnDocumentClick{
        void onClick(View v, int position);
    }
}
