package com.damir.stipancic.blinkstipancic.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.damir.stipancic.blinkstipancic.R;

import java.util.ArrayList;
import java.util.List;

public class DocumentInfoActivityRecyclerAdapter extends RecyclerView.Adapter<DocumentInfoActivityRecyclerAdapter.DocumentInfoActivityViewHolder> {

    private List<String> entryList = new ArrayList<>();

    @NonNull
    @Override
    public DocumentInfoActivityRecyclerAdapter.DocumentInfoActivityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DocumentInfoActivityRecyclerAdapter.DocumentInfoActivityViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.info_recycler_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DocumentInfoActivityRecyclerAdapter.DocumentInfoActivityViewHolder holder, int position) {
        holder.tvEntry.setText(entryList.get(position));
    }

    @Override
    public int getItemCount() {
        return entryList.size();
    }

    public void setList(List<String> entryList) {
        this.entryList = entryList;
        notifyDataSetChanged();
        Log.d("TAG", "datasetchanged: " + entryList.size());
    }

    public class DocumentInfoActivityViewHolder extends RecyclerView.ViewHolder {
        private TextView tvEntry;
        public DocumentInfoActivityViewHolder(@NonNull View itemView) {
            super(itemView);
            tvEntry = itemView.findViewById(R.id.tvInfoEntry);
        }
    }
}
