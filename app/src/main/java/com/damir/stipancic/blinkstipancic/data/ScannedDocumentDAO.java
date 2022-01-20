package com.damir.stipancic.blinkstipancic.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface ScannedDocumentDAO {

    @Query("Select * FROM scanned_document")
    Single<List<ScannedDocument>> loadAllDocuments();

    @Insert
    Completable insertDocument(ScannedDocument document);

    @Delete
    Completable deleteDocument(ScannedDocument documents);
}
