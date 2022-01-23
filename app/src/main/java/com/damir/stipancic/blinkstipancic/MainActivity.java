package com.damir.stipancic.blinkstipancic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

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
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 123;
    private BlinkIdCombinedRecognizer mRecognizer;
    private RecognizerBundle mRecognizerBundle;
    private MainActivityRecyclerAdapter.OnDocumentClick mListener;
    private MainActivityRecyclerAdapter mAdapter;
    private RecyclerView recyclerView;
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


    private void requestStoragePermission() {
        if (ContextCompat.checkSelfPermission(MainActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // request write permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_WRITE_EXTERNAL_STORAGE);
        }

    }

    private void setupScanButton() {
        Button mScanBtn = findViewById(R.id.btnScan);
        mScanBtn.setOnClickListener(view -> {
            requestStoragePermission();
            startScanning();
        });
    }

    private void setupRecycler() {
        recyclerView = findViewById(R.id.rvMainActivity);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));
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
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // permission was granted
                Toast.makeText(this, "Write external storage permission GRANTED!", Toast.LENGTH_SHORT).show();
            } else {
                // permission denied
                Toast.makeText(this, "Write external storage permission is required!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void setDataToRecyclerView() {
        mAdapter = new MainActivityRecyclerAdapter(mPresenter, mListener);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onResponseFailure(Throwable t) {

    }

    @Override
    public void updateRecyclerData() {
        mAdapter.notifyDataSetChanged();
    }
}