package com.damir.stipancic.blinkstipancic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.damir.stipancic.blinkstipancic.adapter.MainActivityRecyclerAdapter;
import com.damir.stipancic.blinkstipancic.viewModels.MainActivityViewModel;
import com.microblink.entities.recognizers.Recognizer;
import com.microblink.entities.recognizers.RecognizerBundle;
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer;
import com.microblink.image.Image;
import com.microblink.uisettings.ActivityRunner;
import com.microblink.uisettings.BlinkIdUISettings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private MainActivityViewModel mMainActivityViewModel;
    private final static int MY_REQUEST_CODE = 100;
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 123;
    private BlinkIdCombinedRecognizer mRecognizer;
    private RecognizerBundle mRecognizerBundle;
    private MainActivityRecyclerAdapter.OnDocumentClick mListener;
    private MainActivityRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestStoragePermission();
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

                // now every recognizer object that was bundled within RecognizerBundle
                // has been updated with results obtained during scanning session

                // you can get the result by invoking getResult on recognizer
                BlinkIdCombinedRecognizer.Result result = mRecognizer.getResult();

                String fullDocumentFrontImageLocation = storeImage("fullDocumentImageFront", result.getFullDocumentFrontImage());
                String fullDocumentBackImageLocation = storeImage("fullDocumentImageBack", result.getFullDocumentBackImage());
                String faceImageLocation = storeImage("faceImage", result.getFaceImage());

                Log.d("TAG", "faceImage: " + faceImageLocation);
                Log.d("TAG", "frontImage: " + fullDocumentFrontImageLocation);
                Log.d("TAG", "backImage: " + fullDocumentBackImageLocation);


                if (result.getResultState() == Recognizer.Result.State.Valid) {
                    // result is valid, you can use it however you wish
                    mMainActivityViewModel.insertDocumentToDB(result, mAdapter, faceImageLocation, fullDocumentFrontImageLocation, fullDocumentBackImageLocation);
                    Intent intent = new Intent(this, DocumentInfoActivity.class);
                    String OIB = result.getPersonalIdNumber();
                    intent.putExtra("OIB", OIB);
                    startActivity(intent);


                }


            }
        }
    }

    private String storeImage(String imageName, Image image) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        Bitmap bitmapImage;
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath=new File(directory,imageName + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage = image.convertToBitmap();
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return mypath.getPath();
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
}