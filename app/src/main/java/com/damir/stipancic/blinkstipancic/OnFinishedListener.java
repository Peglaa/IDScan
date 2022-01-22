package com.damir.stipancic.blinkstipancic;

import com.damir.stipancic.blinkstipancic.data.local.ScannedDocumentEntity;

public interface OnFinishedListener {

    void onFinished(ScannedDocumentEntity scannedDocument);
    void onFailure(Throwable t);
}
