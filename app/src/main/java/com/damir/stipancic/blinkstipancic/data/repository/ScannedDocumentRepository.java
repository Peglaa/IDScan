package com.damir.stipancic.blinkstipancic.data.repository;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.util.Log;

import com.damir.stipancic.blinkstipancic.contract.InfoContract;
import com.damir.stipancic.blinkstipancic.contract.MainContract;
import com.damir.stipancic.blinkstipancic.data.local.ScannedDocumentDatabase;
import com.damir.stipancic.blinkstipancic.data.local.ScannedDocumentEntity;
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer;
import com.microblink.image.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class ScannedDocumentRepository{

    private final ScannedDocumentDatabase mDocumentDatabase;

    public ScannedDocumentRepository(Context context) {
        this.mDocumentDatabase = ScannedDocumentDatabase.getInstance(context);
    }

    public void insertDataToDB(BlinkIdCombinedRecognizer.Result result, Context context, MainContract.Presenter.OnFinishedListener onFinishedListener) {
        Log.d("TAG", "OIB: " + result.getPersonalIdNumber());
        String firstName = result.getFirstName();
        String lastName = result.getLastName();
        String gender = result.getSex();
        String OIB = result.getPersonalIdNumber();
        String dateOfBirth = null;
        if(result.getDateOfBirth().getDate() != null)
            dateOfBirth = result.getDateOfBirth().getDate().toString();
        String nationality = result.getNationality();
        String documentNumber = result.getDocumentNumber();
        String dateOfExpiry = result.getDateOfExpiry().toString();
        if(result.getDateOfExpiry().getDate() != null)
          dateOfExpiry = result.getDateOfExpiry().getDate().toString();
        String faceImage = storeImage("faceImage", result.getFaceImage(), result.getLastName(), context);
        String frontImage = storeImage("frontImage", result.getFullDocumentFrontImage(), result.getLastName(), context);
        String backImage = storeImage("backImage", result.getFullDocumentBackImage(), result.getLastName(), context);

        ScannedDocumentEntity document = new ScannedDocumentEntity.ScannedDocumentBuilder(firstName, lastName)
                .OIB(OIB)
                .gender(gender)
                .dateOfBirth(dateOfBirth)
                .nationality(nationality)
                .documentNumber(documentNumber)
                .dateOfExpiry(dateOfExpiry)
                .faceImage(faceImage)
                .frontImage(frontImage)
                .backImage(backImage)
                .build();

        mDocumentDatabase.documentDAO().insertDocument(document).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                mDocumentDatabase.documentDAO().deleteExtraDocument().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        getScannedDocumentListFromDB(onFinishedListener);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        getScannedDocumentListFromDB(onFinishedListener);
                    }
                });
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }
        });
    }

    public void getScannedDocumentListFromDB(MainContract.Presenter.OnFinishedListener onFinishedListener) {
        mDocumentDatabase.documentDAO().loadAllDocuments().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<ScannedDocumentEntity>>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<ScannedDocumentEntity> scannedDocumentEntities) {
                Log.d("TAG", "SIZE: " + scannedDocumentEntities.size());
                onFinishedListener.onFinished(scannedDocumentEntities);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }
        });
    }

    public void getDocumentByID(int id, InfoContract.Presenter.OnFinishedListener listener){
        mDocumentDatabase.documentDAO().loadDocumentByID(id).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ScannedDocumentEntity>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ScannedDocumentEntity scannedDocumentEntity) {

                boolean isExpired = checkDate(scannedDocumentEntity);
                listener.onFinished(scannedDocumentEntity, isExpired);

            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }
        });

    }

    public void getDocumentByOIB(String OIB, InfoContract.Presenter.OnFinishedListener listener){
        mDocumentDatabase.documentDAO().loadDocumentByOIB(OIB).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ScannedDocumentEntity>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ScannedDocumentEntity scannedDocumentEntity) {

                boolean isExpired = checkDate(scannedDocumentEntity);
                listener.onFinished(scannedDocumentEntity, isExpired);

            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }
        });

    }

    private String storeImage(String imageName, Image image, String name, Context context) {
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        Bitmap bitmapImage;
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File myPath=new File(directory,imageName + name + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage = image.convertToBitmap();
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return myPath.getPath();
    }

    private boolean checkDate(ScannedDocumentEntity scannedDocumentEntity) {
        Date expDate = toDate(scannedDocumentEntity.getDateOfExpiry());
        return new Date().after(expDate);
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
}
