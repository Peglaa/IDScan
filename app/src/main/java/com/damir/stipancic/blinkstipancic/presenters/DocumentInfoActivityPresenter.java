package com.damir.stipancic.blinkstipancic.presenters;

import com.damir.stipancic.blinkstipancic.BlinkApplication;
import com.damir.stipancic.blinkstipancic.contract.Contract;
import com.damir.stipancic.blinkstipancic.data.local.ScannedDocumentEntity;
import com.damir.stipancic.blinkstipancic.data.repository.ScannedDocumentRepository;

public class DocumentInfoActivityPresenter implements Contract.Presenter.DocumentInfoActivityPresenter, Contract.Presenter.PresenterOnFinishedListener {

    private final ScannedDocumentRepository mScannedDocumentRepository;
    private final Contract.View.DocumentInfoActivityView mDocumentActivityView;

    public DocumentInfoActivityPresenter(Contract.View.DocumentInfoActivityView documentView){
        mScannedDocumentRepository = new ScannedDocumentRepository(BlinkApplication.getAppContext());
        mDocumentActivityView = documentView;
    }

    @Override
    public void onFinished(ScannedDocumentEntity scannedDocumentEntity) {
        mDocumentActivityView.getDocument(scannedDocumentEntity);
    }

    @Override
    public void onFailure(Throwable t) {

    }

    @Override
    public void getDocumentByIDFromDB(int ID) {
        mScannedDocumentRepository.getDocumentByID(ID, this);
    }
}
