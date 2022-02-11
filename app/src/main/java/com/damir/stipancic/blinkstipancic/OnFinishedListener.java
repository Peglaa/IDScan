package com.damir.stipancic.blinkstipancic;

import com.damir.stipancic.blinkstipancic.data.local.ScannedDocumentEntity;

import java.util.List;

public interface OnFinishedListener {
    void onFinished(List<ScannedDocumentEntity> scannedDocumentEntities);
    void onFailure(Throwable t);
}
