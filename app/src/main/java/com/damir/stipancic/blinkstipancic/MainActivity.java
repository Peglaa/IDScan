package com.damir.stipancic.blinkstipancic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.damir.stipancic.blinkstipancic.adapter.MainActivityRecyclerAdapter;
import com.damir.stipancic.blinkstipancic.contract.Contract;
import com.damir.stipancic.blinkstipancic.presenters.MainActivityPresenter;
import com.microblink.entities.recognizers.Recognizer;
import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer;
import com.microblink.uisettings.ActivityRunner;
import com.microblink.uisettings.BlinkIdUISettings;

public class MainActivity extends AppCompatActivity implements Contract.View.MainActivityView{

    private final static int MY_REQUEST_CODE = 100;
    private BlinkIdCombinedRecognizer mRecognizer;
    private RecognizerBundle mRecognizerBundle;
    private MainActivityRecyclerAdapter.OnDocumentClick mListener;
    private RecyclerView mRecyclerView;
    private MainActivityPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPresenter = new MainActivityPresenter(this);
        setupScanButton();
        setupOnClickListener();
        setupRecycler();
        fetchRecyclerData();
        setupBlinkRecognizer();
    }

    private void setupOnClickListener() {
        mListener = (v, position) -> {

            Intent intent = new Intent(v.getContext(), DocumentInfoActivity.class);
            String OIB = mPresenter.getOIB(position);
            intent.putExtra("OIB", OIB);
            startActivity(intent);

        };
    }

    private void setupScanButton() {
        Button mScanBtn = findViewById(R.id.btnScan);
        mScanBtn.setOnClickListener(view -> startScanning());
    }

    private void setupRecycler() {
        mRecyclerView = findViewById(R.id.rvMainActivity);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(mRecyclerView.getContext(), DividerItemDecoration.VERTICAL));
    }

    private void fetchRecyclerData() {
        mPresenter.getScannedDocumentListFromDB();
    }

    private void setupBlinkRecognizer() {
        mRecognizer = new BlinkIdCombinedRecognizer();
        mRecognizer.setReturnFullDocumentImage(true);
        mRecognizer.setReturnFaceImage(true);
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
                BlinkIdCombinedRecognizer.Result result = mRecognizer.getResult();

                if (result.getResultState() == Recognizer.Result.State.Valid) {
                    // Send result to ViewModel and create a class object to store in Room
                    mPresenter.insertDocumentToDB(result);
                    Intent intent = new Intent(this, DocumentInfoActivity.class);
                    String OIB = result.getPersonalIdNumber();
                    intent.putExtra("OIB", OIB);
                    startActivity(intent);

                }
            }
        }
    }

    @Override
    public void setDataToRecyclerView() {
        MainActivityRecyclerAdapter mAdapter = new MainActivityRecyclerAdapter(mPresenter, mListener);
        mRecyclerView.setAdapter(mAdapter);
    }
}