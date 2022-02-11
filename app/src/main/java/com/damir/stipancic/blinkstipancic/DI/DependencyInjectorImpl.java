package com.damir.stipancic.blinkstipancic.DI;

import com.damir.stipancic.blinkstipancic.BlinkApplication;
import com.damir.stipancic.blinkstipancic.data.repository.ScannedDocumentRepository;

public class DependencyInjectorImpl implements DependencyInjector{

    @Override
    public ScannedDocumentRepository scannedDocumentRepository() {
        return new ScannedDocumentRepository(BlinkApplication.getAppContext());
    }
}
