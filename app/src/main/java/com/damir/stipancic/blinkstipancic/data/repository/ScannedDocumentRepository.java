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
        String faceImage = storeImage("faceImage", result.getFaceImage(), result.getPersonalIdNumber(), context);
        String frontImage = storeImage("frontImage", result.getFullDocumentFrontImage(), result.getPersonalIdNumber(), context);
        String backImage = storeImage("backImage", result.getFullDocumentBackImage(), result.getPersonalIdNumber(), context);

        ScannedDocumentEntity document = new ScannedDocumentEntity(firstName, lastName, gender, OIB, dateOfBirth, nationality, documentNumber, dateOfExpiry, faceImage, frontImage, backImage);

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

                listener.onFinished(scannedDocumentEntity);

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

                listener.onFinished(scannedDocumentEntity);

            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }
        });

    }

    private String storeImage(String imageName, Image image, String OIB, Context context) {
        ContextWrapper cw = new ContextWrapper(context.getApplicationContext());
        Bitmap bitmapImage;
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File myPath=new File(directory,imageName + OIB + ".jpg");

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
}
