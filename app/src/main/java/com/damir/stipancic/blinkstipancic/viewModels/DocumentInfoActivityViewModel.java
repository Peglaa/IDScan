package com.damir.stipancic.blinkstipancic.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.damir.stipancic.blinkstipancic.adapters.DocumentInfoActivityRecyclerAdapter;
import com.damir.stipancic.blinkstipancic.data.ScannedDocumentDatabase;
import com.damir.stipancic.blinkstipancic.data.ScannedDocumentEntity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.SingleObserver;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class DocumentInfoActivityViewModel extends AndroidViewModel {

    private ScannedDocumentDatabase mDocumentDatabase;
    private DocumentInfoActivityRecyclerAdapter adapter;


    public DocumentInfoActivityViewModel(@NonNull Application application) {
        super(application);

        mDocumentDatabase = ScannedDocumentDatabase.getInstance(application.getApplicationContext());
        adapter = new DocumentInfoActivityRecyclerAdapter();
    }

    public void getDocumentByOIB(String oib){
        mDocumentDatabase.documentDAO().loadDocumentByOIB(oib).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleObserver<ScannedDocumentEntity>() {
            @Override
            public void onSubscribe(@io.reactivex.rxjava3.annotations.NonNull Disposable d) {

            }

            @Override
            public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ScannedDocumentEntity scannedDocumentEntity) {

                adapter.setList(getInfo(scannedDocumentEntity));

            }

            @Override
            public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {

            }
        });



}

    private List<String> getInfo(ScannedDocumentEntity scannedDocumentEntity) {
        List<String> list = new ArrayList<>();
        list.add(scannedDocumentEntity.getGender());
        list.add(scannedDocumentEntity.getDateOfBirth());
        list.add(scannedDocumentEntity.getOIB());
        list.add(scannedDocumentEntity.getNationality());
        list.add(scannedDocumentEntity.getDocumentNumber());
        list.add(scannedDocumentEntity.getDateOfExpiry());
        return list;
    }

    public DocumentInfoActivityRecyclerAdapter getAdapter(){

        return adapter;
    }
}
