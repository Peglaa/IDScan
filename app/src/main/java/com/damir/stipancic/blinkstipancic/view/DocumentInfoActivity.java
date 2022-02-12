package com.damir.stipancic.blinkstipancic.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.damir.stipancic.blinkstipancic.DI.DependencyInjectorImpl;
import com.damir.stipancic.blinkstipancic.R;
import com.damir.stipancic.blinkstipancic.contract.InfoContract;
import com.damir.stipancic.blinkstipancic.data.local.ScannedDocumentEntity;
import com.damir.stipancic.blinkstipancic.presenters.DocumentInfoPresenter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DocumentInfoActivity extends AppCompatActivity implements InfoContract.View {

    private TextView mFirstNameTV, mLastNameTV, mGenderTV, mDateOfBirthTV, mOibTV, mNationalityTV, mDocumentNumberTV, mDateOfExpiryTV;
    private ImageView mFrontImage, mFaceImage, mBackImage;
    private InfoContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_info);

        setPresenter(new DocumentInfoPresenter(this, new DependencyInjectorImpl()));
        setupLayout();
        Intent intent = getIntent();
        int ID = intent.getIntExtra("ID", -1);
        String OIB = intent.getStringExtra("OIB");

        if(ID != -1)
            mPresenter.getDocumentByIDFromDB(ID);
        else{
            mPresenter.getDocumentByOIBFromDB(OIB);
        }

    }

    private void setupLayout() {
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
        Log.d("TAG", "FACEIMAGE: " + scannedDocument.getFaceImage());
        loadImageFromStorage(scannedDocument.getFaceImage(), mFaceImage);
        Log.d("TAG", "FRONTIMAGE: " + scannedDocument.getFrontImage());
        loadImageFromStorage(scannedDocument.getFrontImage(), mFrontImage);
        Log.d("TAG", "BACKIMAGE: " + scannedDocument.getBackImage());
        loadImageFromStorage(scannedDocument.getBackImage(), mBackImage);

    }

    private void loadImageFromStorage(String path, ImageView imageView)
    {
        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap;
        options.inSampleSize = 1;
        bitmap = BitmapFactory.decodeFile(path, options);

        //Bitmap bitmap2 = Bitmap.createScaledBitmap(bitmap1,  600 ,600, true);//this bitmap2 you can use only for display
        imageView.setImageBitmap(bitmap);
        Log.d("TAG", "loadImageFromStorage: ");

    }

    private void checkDateOfExpiry(String dateOfExpiry) {
        Date expDate = toDate(dateOfExpiry);
        if (new Date().after(expDate))
            Toast.makeText(this, "ID HAS EXPIRED!", Toast.LENGTH_SHORT).show();
    }

    private Date toDate(String dateOfExpiry) {
        Date expDate = null;
        Log.d("TAG", "toExpDate: " + dateOfExpiry);
        SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy.");
        try {
            expDate = format.parse(dateOfExpiry);
            Log.d("TAG", "toDate: " + expDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return expDate;
    }

    @Override
    public void setDataToView(ScannedDocumentEntity scannedDocument) {
        displayInformation(scannedDocument);
        checkDateOfExpiry(scannedDocument.getDateOfExpiry());
    }

    @Override
    public void getDocument(ScannedDocumentEntity scannedDocumentEntity) {
        setDataToView(scannedDocumentEntity);
    }

    @Override
    public void setPresenter(InfoContract.Presenter presenter) {
        mPresenter = presenter;
    }

    @Override
    protected void onDestroy() {
        mPresenter.onDestroy();
        super.onDestroy();
    }
}