package com.damir.stipancic.blinkstipancic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.damir.stipancic.blinkstipancic.viewModels.DocumentInfoActivityViewModel;

public class DocumentInfoActivity extends AppCompatActivity {

    private TextView mFullNameText;
    private ImageView mFrontImage, mFaceImage, mBackImage;
    private RecyclerView mInfoRecycler;
    private DocumentInfoActivityViewModel infoViewModel;
    private String mOIB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_info);

        Intent intent = getIntent();
        mOIB = intent.getStringExtra("OIB");
        infoViewModel = new DocumentInfoActivityViewModel(getApplication());
        mFullNameText = findViewById(R.id.tvInfoFullName);
        mFrontImage = findViewById(R.id.ivInfoFrontImage);
        mFaceImage = findViewById(R.id.ivInfoFaceImage);
        mBackImage = findViewById(R.id.ivInfoBackImage);
        setupRecycler();
        infoViewModel.getDocumentByOIB(mOIB);
    }

    private void setupRecycler() {
        mInfoRecycler = findViewById(R.id.rvInfoDocument);
        mInfoRecycler.setLayoutManager(new LinearLayoutManager(this));
        mInfoRecycler.addItemDecoration(new DividerItemDecoration(mInfoRecycler.getContext(), DividerItemDecoration.VERTICAL));
        mInfoRecycler.setAdapter(infoViewModel.getAdapter());
    }
}