package com.damir.stipancic.blinkstipancic.DI;

import com.damir.stipancic.blinkstipancic.data.repository.ScannedDocumentRepository;

public interface DependencyInjector {
    ScannedDocumentRepository scannedDocumentRepository();
}
