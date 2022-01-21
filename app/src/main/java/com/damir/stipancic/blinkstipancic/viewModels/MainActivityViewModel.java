package com.damir.stipancic.blinkstipancic.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.damir.stipancic.blinkstipancic.data.ScannedDocumentDatabase;
import com.damir.stipancic.blinkstipancic.data.ScannedDocumentEntity;
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer;
import com.microblink.results.date.Date;

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
        Log.d("TAG", "getDocumentsFromDB: " + documentList.size());
        if(documentList.size() > 0)
        {
            mListOfDocuments.postValue(documentList);
        }else {
            mListOfDocuments.postValue(null);
        }
    }

    public void insertDocumentToDB(BlinkIdCombinedRecognizer.Result result){

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

        mDocumentDatabase.documentDAO().insertDocument(document);
        getDocumentsFromDB();

    }

    public void deleteDocumentFromDB(ScannedDocumentEntity document){

        mDocumentDatabase.documentDAO().deleteDocument(document);
        getDocumentsFromDB();
    }
}
