package com.damir.stipancic.blinkstipancic.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.damir.stipancic.blinkstipancic.R;
import com.damir.stipancic.blinkstipancic.data.ScannedDocumentEntity;

import java.util.ArrayList;
import java.util.List;

public class MainActivityRecyclerAdapter extends RecyclerView.Adapter<MainActivityRecyclerAdapter.MainActivityRecyclerViewHolder> {

    private List<ScannedDocumentEntity> documentList = new ArrayList<>();
    private final OnDocumentClick mListener;

    public MainActivityRecyclerAdapter(OnDocumentClick listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public MainActivityRecyclerAdapter.MainActivityRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainActivityRecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_item, parent, false), mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivityRecyclerAdapter.MainActivityRecyclerViewHolder holder, int position) {
        holder.tvDocument.setText(documentList.get(position).getOIB());
    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }

    public List<ScannedDocumentEntity> getList(){
        return documentList;
    }

    public void setList(List<ScannedDocumentEntity> documentList) {
        this.documentList = documentList;
        notifyDataSetChanged();
        Log.d("TAG", "datasetchanged: " + documentList.size());
    }

    public class MainActivityRecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tvDocument;
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
    }

    public interface OnDocumentClick{
        void onClick(View v, int position);
    }
}
