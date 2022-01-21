package com.damir.stipancic.blinkstipancic.viewModels;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.damir.stipancic.blinkstipancic.adapters.MainActivityRecyclerAdapter;
import com.damir.stipancic.blinkstipancic.data.ScannedDocumentDatabase;
import com.damir.stipancic.blinkstipancic.data.ScannedDocumentEntity;
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer;
import com.microblink.results.date.Date;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.CompletableObserver;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainActivityViewModel extends AndroidViewModel {

    private List<ScannedDocumentEntity> mListOfDocuments;
    private ScannedDocumentDatabase mDocumentDatabase;
    private MainActivityRecyclerAdapter adapter;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        mListOfDocuments = new ArrayList<>();
        mDocumentDatabase = ScannedDocumentDatabase.getInstance(application.getApplicationContext());
        adapter = new MainActivityRecyclerAdapter();
    }

    public void getDocumentsFromDB(){
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

    public MainActivityRecyclerAdapter getAdapter(){

        return adapter;
    }

    public void insertDocumentToDB(BlinkIdCombinedRecognizer.Result result){

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
        String faceImage = "blabla";
        String frontImage = "blabla";
        String backImage = "blabla";


        ScannedDocumentEntity document = new ScannedDocumentEntity(firstName, lastName, gender, OIB, dateOfBirth, nationality, documentNumber, dateOfExpiry, faceImage, frontImage, backImage);

        mDocumentDatabase.documentDAO().insertDocument(document).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new CompletableObserver() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }

            @Override
            public void onComplete() {
                getDocumentsFromDB();
            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }
        });


    }

    public void deleteDocumentFromDB(ScannedDocumentEntity document){

        mDocumentDatabase.documentDAO().deleteDocument(document);
        getDocumentsFromDB();
    }
}
