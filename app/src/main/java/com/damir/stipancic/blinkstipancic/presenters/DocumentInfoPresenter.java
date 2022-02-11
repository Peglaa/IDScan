package com.damir.stipancic.blinkstipancic.presenters;

import com.damir.stipancic.blinkstipancic.DI.DependencyInjectorImpl;
import com.damir.stipancic.blinkstipancic.contract.InfoContract;
import com.damir.stipancic.blinkstipancic.data.local.ScannedDocumentEntity;
import com.damir.stipancic.blinkstipancic.data.repository.ScannedDocumentRepository;


public class DocumentInfoPresenter implements InfoContract.Presenter, InfoContract.Presenter.OnFinishedListener {

    private InfoContract.View mView;
    private final ScannedDocumentRepository mRepository;

    public DocumentInfoPresenter(InfoContract.View view, DependencyInjectorImpl dependencyInjector){
        this.mView = view;
        this.mRepository = dependencyInjector.scannedDocumentRepository();
    }
    @Override
    public void getDocumentByIDFromDB(int ID) {
        mRepository.getDocumentByID(ID, this);
    }

    @Override
    public void getDocumentByOIBFromDB(String OIB) {
        mRepository.getDocumentByOIB(OIB, this);
    }

    @Override
    public void onDestroy() {
        this.mView = null;
    }

    @Override
    public void onFinished(ScannedDocumentEntity scannedDocumentEntity) {
        mView.getDocument(scannedDocumentEntity);
    }

    @Override
    public void onFailure(Throwable t) {

    }
}
