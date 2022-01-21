package com.damir.stipancic.blinkstipancic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.damir.stipancic.blinkstipancic.adapters.DocumentInfoActivityRecyclerAdapter;
import com.damir.stipancic.blinkstipancic.data.ScannedDocumentEntity;
import com.damir.stipancic.blinkstipancic.viewModels.DocumentInfoActivityViewModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class DocumentInfoActivity extends AppCompatActivity implements OnFinishedListener{

    private TextView mFirstNameTV, mLastNameTV, mGenderTV, mDateOfBirthTV, mOibTV, mNationalityTV, mDocumentNumberTV, mDateOfExpiryTV;
    private ScannedDocumentEntity mScannedDocument;
    private ImageView mFrontImage, mFaceImage, mBackImage;
    private DocumentInfoActivityViewModel infoViewModel;
    private String mOIB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_info);

        Intent intent = getIntent();
        mOIB = intent.getStringExtra("OIB");

        infoViewModel = new DocumentInfoActivityViewModel(getApplication());
        infoViewModel.getDocumentByOIB(mOIB, this);
        mFirstNameTV = findViewById(R.id.tvInfoFirstName);
        mLastNameTV = findViewById(R.id.tvInfoLastName);
        mGenderTV = findViewById(R.id.tvInfoGender);
        mOibTV = findViewById(R.id.tvInfoOIB);
        mDateOfBirthTV = findViewById(R.id.tvInfoDateOfBirth);
        mNationalityTV = findViewById(R.id.tvInfoNationality);
        mDocumentNumberTV = findViewById(R.id.tvInfoDocumentNumber);
        mDateOfExpiryTV = findViewById(R.id.tvInfoDateOfExpiry);
        mFrontImage = findViewById(R.id.ivInfoFrontImage);
        mFaceImage = findViewById(R.id.ivInfoFaceImage);
        mBackImage = findViewById(R.id.ivInfoBackImage);

    }

    private void displayInformation(ScannedDocumentEntity scannedDocument) {
        mFirstNameTV.setText(getString(R.string.FirstName, scannedDocument.getFirstName()));
        mLastNameTV.setText(getString(R.string.LastName, scannedDocument.getLastName()));
        mGenderTV.setText(getString(R.string.Gender, scannedDocument.getGender()));
        mDateOfBirthTV.setText(getString(R.string.DateOfBirth, scannedDocument.getDateOfBirth()));
        mOibTV.setText(getString(R.string.OIB, scannedDocument.getOIB()));
        mNationalityTV.setText(getString(R.string.Nationality, scannedDocument.getNationality()));
        mDocumentNumberTV.setText(getString(R.string.DocumentNumber, scannedDocument.getDocumentNumber()));
        mDateOfExpiryTV.setText(getString(R.string.DateOfExpiry, scannedDocument.getDateOfExpiry()));

    }

    private void loadImageFromStorage(String path, ImageView imageView)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap;
        options.inSampleSize = 16;
        bitmap = BitmapFactory.decodeFile(path, options);

        //Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1,  600 ,600, true);//this bitmap2 you can use only for display
        imageView.setImageBitmap(bitmap);
        Log.d("TAG", "loadImageFromStorage: ");

    }

    @Override
    public void onFinished(ScannedDocumentEntity scannedDocument) {
        mScannedDocument = scannedDocument;
        Log.d("TAG", "onCreate: " + mScannedDocument.toString());
        displayInformation(mScannedDocument);
    }

    @Override
    public void onFailure(Throwable t) {

    }
}