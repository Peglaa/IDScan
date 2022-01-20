package com.damir.stipancic.blinkstipancic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.damir.stipancic.blinkstipancic.data.ScannedDocumentEntity;
import com.damir.stipancic.blinkstipancic.viewModels.MainActivityViewModel;
import com.microblink.entities.recognizers.Recognizer;
import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer;
import com.microblink.uisettings.ActivityRunner;
import com.microblink.uisettings.BlinkIdUISettings;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mMainActivityViewModel;
    private final static int MY_REQUEST_CODE = 100;
    private Button mScanBtn;
    private BlinkIdCombinedRecognizer mRecognizer;
    private RecognizerBundle mRecognizerBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mScanBtn = findViewById(R.id.btnScan);
        mScanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScanning();
            }
        });

        mRecognizer = new BlinkIdCombinedRecognizer();
        mRecognizerBundle = new RecognizerBundle(mRecognizer);

        initViewModel();
    }

    private void initViewModel() {

        mMainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mMainActivityViewModel.getDocumentListObserver().observe(this, new Observer<List<ScannedDocumentEntity>>() {
            @Override
            public void onChanged(List<ScannedDocumentEntity> scannedDocumentEntities) {
                if(scannedDocumentEntities == null){
                    //noResulttextView.setVisibility(View.VISIBLE);
                    //recyclerView.setVisibility(View.GONE);
                }
                else{
                    //noResulttextView.setVisibility(View.Gone);
                    //recyclerView.setVisibility(View.VISIBLE)
                }
            }
        });
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



                }


            }
        }
    }
}