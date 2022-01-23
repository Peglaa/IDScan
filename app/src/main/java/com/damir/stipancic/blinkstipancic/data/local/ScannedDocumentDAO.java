package com.damir.stipancic.blinkstipancic.data.local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

@Dao
public interface ScannedDocumentDAO {

    @Query("SELECT * FROM scanned_document")
    Single<List<ScannedDocumentEntity>> loadAllDocuments();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertDocument(ScannedDocumentEntity document);

    @Delete
    Completable deleteDocument(ScannedDocumentEntity documents);

    @Query("SELECT * FROM scanned_document WHERE OIB = :oib")
    Single<ScannedDocumentEntity> loadDocumentByOIB(String oib);

    @Query("DELETE FROM scanned_document where id NOT IN (SELECT id from scanned_document ORDER BY id DESC LIMIT 5)")
    Completable deleteExtraDocument();
}
