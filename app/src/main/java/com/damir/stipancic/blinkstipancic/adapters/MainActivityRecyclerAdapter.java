package com.damir.stipancic.blinkstipancic.adapters;

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

    @NonNull
    @Override
    public MainActivityRecyclerAdapter.MainActivityRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MainActivityRecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.main_recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MainActivityRecyclerAdapter.MainActivityRecyclerViewHolder holder, int position) {
        holder.tvDocument.setText(documentList.get(position).getOIB());
    }

    @Override
    public int getItemCount() {
        return documentList.size();
    }

    public void setList(List<ScannedDocumentEntity> documentList) {
        this.documentList = documentList;
        notifyDataSetChanged();
        Log.d("TAG", "datasetchanged: " + documentList.size());
    }

    public class MainActivityRecyclerViewHolder extends RecyclerView.ViewHolder {
        private TextView tvDocument;
        public MainActivityRecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDocument = itemView.findViewById(R.id.tvDocument);
        }
    }
}
