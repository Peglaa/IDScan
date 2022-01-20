package com.damir.stipancic.blinkstipancic.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.damir.stipancic.blinkstipancic.data.ScannedDocumentDatabase;
import com.damir.stipancic.blinkstipancic.data.ScannedDocumentEntity;

import java.util.List;

public class MainActivityViewModel extends AndroidViewModel {

    private MutableLiveData<List<ScannedDocumentEntity>> mListOfDocuments;
    private ScannedDocumentDatabase mDocumentDatabase;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        mListOfDocuments = new MutableLiveData<>();
        mDocumentDatabase = ScannedDocumentDatabase.getInstance(application.getApplicationContext());
    }

    public MutableLiveData<List<ScannedDocumentEntity>> getDocumentListObserver(){
        return mListOfDocuments;
    }

    public void getDocumentsFromDB(){
        List<ScannedDocumentEntity> documentList = mDocumentDatabase.documentDAO().loadAllDocuments();
        if(documentList.size() > 0)
        {
            mListOfDocuments.postValue(documentList);
        }else {
            mListOfDocuments.postValue(null);
        }
    }

    public void insertDocumentToDB(ScannedDocumentEntity document){

        mDocumentDatabase.documentDAO().insertDocument(document);
        getDocumentsFromDB();

    }

    public void deleteDocumentFromDB(ScannedDocumentEntity document){

        mDocumentDatabase.documentDAO().deleteDocument(document);
        getDocumentsFromDB();
    }
}
