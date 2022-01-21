package com.damir.stipancic.blinkstipancic.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.damir.stipancic.blinkstipancic.adapter.MainActivityRecyclerAdapter;
import com.damir.stipancic.blinkstipancic.data.ScannedDocumentDatabase;
import com.damir.stipancic.blinkstipancic.data.ScannedDocumentEntity;
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivityViewModel extends AndroidViewModel {

    private ScannedDocumentDatabase mDocumentDatabase;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        mDocumentDatabase = ScannedDocumentDatabase.getInstance(application.getApplicationContext());
    }

    public void getDocumentsFromDB(MainActivityRecyclerAdapter adapter){
        mDocumentDatabase.documentDAO().loadAllDocuments().subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<List<ScannedDocumentEntity>>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull List<ScannedDocumentEntity> scannedDocumentEntities) {
                Log.d("TAG", "SIZE: " + scannedDocumentEntities.size());
                adapter.setList(scannedDocumentEntities);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }
        });


    }

    public String getOIB(int position, MainActivityRecyclerAdapter adapter){
        return adapter.getList().get(position).getOIB();
    }

    public void insertDocumentToDB(BlinkIdCombinedRecognizer.Result result, MainActivityRecyclerAdapter adapter, String faceImageLocation, String frontImageLocation, String backImageLocation){

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
        String dateOfExpiry = null;
        if(result.getDateOfExpiry().getDate() != null)
            dateOfExpiry = result.getDateOfExpiry().getDate().toString();
        String faceImage = null, frontImage = null, backImage = null;
        if(faceImageLocation != null)
            faceImage = faceImageLocation;
        if(frontImageLocation != null)
            frontImage = frontImageLocation;
        if(backImageLocation != null)
            backImage = backImageLocation;


        ScannedDocumentEntity document = new ScannedDocumentEntity(firstName, lastName, gender, OIB, dateOfBirth, nationality, documentNumber, dateOfExpiry, faceImage, frontImage, backImage);

        mDocumentDatabase.documentDAO().insertDocument(document).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                getDocumentsFromDB(adapter);
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }
        });


    }

    public void deleteDocumentFromDB(ScannedDocumentEntity document, MainActivityRecyclerAdapter adapter){

        mDocumentDatabase.documentDAO().deleteDocument(document);
        getDocumentsFromDB(adapter);
    }
}
