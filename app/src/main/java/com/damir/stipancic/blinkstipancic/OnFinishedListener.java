package com.damir.stipancic.blinkstipancic;

import com.damir.stipancic.blinkstipancic.data.ScannedDocumentEntity;

public interface OnFinishedListener {

    void onFinished(ScannedDocumentEntity scannedDocument);
    void onFailure(Throwable t);
}
