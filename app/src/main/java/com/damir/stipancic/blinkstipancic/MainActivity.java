package com.damir.stipancic.blinkstipancic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.damir.stipancic.blinkstipancic.adapters.MainActivityRecyclerAdapter;
import com.damir.stipancic.blinkstipancic.viewModels.MainActivityViewModel;
import com.microblink.entities.recognizers.Recognizer;
import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer;
import com.microblink.uisettings.ActivityRunner;
import com.microblink.uisettings.BlinkIdUISettings;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mMainActivityViewModel;
    private final static int MY_REQUEST_CODE = 100;
    private BlinkIdCombinedRecognizer mRecognizer;
    private RecognizerBundle mRecognizerBundle;
    private MainActivityRecyclerAdapter.OnDocumentClick mListener;
    private MainActivityRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setupScanButton();
        setupViewModel();
        setupOnClickListener();
        setupRecycler();
        fetchRecyclerData();
        setupBlinkRecognizer();


    }

    private void setupOnClickListener() {
        mListener = (v, position) -> {
            Intent intent = new Intent(v.getContext(), DocumentInfoActivity.class);
            String OIB = mMainActivityViewModel.getOIB(position, mAdapter);
            intent.putExtra("OIB", OIB);
            startActivity(intent);
        };
    }

    private void setupScanButton() {
        Button mScanBtn = findViewById(R.id.btnScan);
        mScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScanning();
            }
        });
    }

    private void setupViewModel() {
        mMainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
    }

    private void setupRecycler() {
        RecyclerView recyclerView = findViewById(R.id.rvMainActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
        mAdapter = new MainActivityRecyclerAdapter(mListener, this);
        recyclerView.setAdapter(mAdapter);
    }

    private void fetchRecyclerData() {
        mMainActivityViewModel.getDocumentsFromDB(mAdapter);
    }

    private void setupBlinkRecognizer() {
        mRecognizer = new BlinkIdCombinedRecognizer();
        mRecognizerBundle = new RecognizerBundle(mRecognizer);
    }

    public void startScanning() {
        // Settings for BlinkIdActivity
        BlinkIdUISettings settings = new BlinkIdUISettings(mRecognizerBundle);

        // Start activity
        ActivityRunner.startActivityForResult(this, MY_REQUEST_CODE, settings);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                // load the data into all recognizers bundled within your RecognizerBundle
                mRecognizerBundle.loadFromIntent(data);

                // now every recognizer object that was bundled within RecognizerBundle
                // has been updated with results obtained during scanning session

                // you can get the result by invoking getResult on recognizer
                BlinkIdCombinedRecognizer.Result result = mRecognizer.getResult();
                if (result.getResultState() == Recognizer.Result.State.Valid) {
                    // result is valid, you can use it however you wish
                    mMainActivityViewModel.insertDocumentToDB(result, mAdapter);


                }


            }
        }
    }
}