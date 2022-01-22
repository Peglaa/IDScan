package com.damir.stipancic.blinkstipancic.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.damir.stipancic.blinkstipancic.OnFinishedListener;
import com.damir.stipancic.blinkstipancic.data.local.ScannedDocumentDatabase;
import com.damir.stipancic.blinkstipancic.data.local.ScannedDocumentEntity;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DocumentInfoActivityViewModel extends AndroidViewModel {

    private ScannedDocumentDatabase mDocumentDatabase;
    private ScannedDocumentEntity mScannedDocumentEntity;


    public DocumentInfoActivityViewModel(@NonNull Application application) {
        super(application);

        mDocumentDatabase = ScannedDocumentDatabase.getInstance(application.getApplicationContext());
    }

    public void getDocumentByOIB(String oib, OnFinishedListener listener){
        mDocumentDatabase.documentDAO().loadDocumentByOIB(oib).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ScannedDocumentEntity>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ScannedDocumentEntity scannedDocumentEntity) {

                mScannedDocumentEntity = scannedDocumentEntity;
                listener.onFinished(mScannedDocumentEntity);


            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }
        });

    }

}
