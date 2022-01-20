package com.damir.stipancic.blinkstipancic.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(
        entities = {ScannedDocument.class},
        version = 1,
        exportSchema = false
)
@TypeConverters({Converters.class})
abstract class ScannedDocumentDatabase extends RoomDatabase {

    public static ScannedDocumentDatabase INSTANCE;

    public abstract ScannedDocumentDAO documentDAO();

    public static synchronized ScannedDocumentDatabase getInstance(Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    ScannedDocumentDatabase.class, "scanned_document_database")
                    .build();
        }
        return INSTANCE;
    }
}
