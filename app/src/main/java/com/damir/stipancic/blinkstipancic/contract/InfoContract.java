package com.damir.stipancic.blinkstipancic.contract;

import com.damir.stipancic.blinkstipancic.data.local.ScannedDocumentEntity;
import com.damir.stipancic.blinkstipancic.presenters.BasePresenter;
import com.damir.stipancic.blinkstipancic.view.BaseView;

public interface InfoContract {
    interface Presenter extends BasePresenter{

        interface OnFinishedListener {
            void onFinished(ScannedDocumentEntity scannedDocumentEntity);
            void onFailure(Throwable t);
        }

        void getDocumentByIDFromDB(int ID);
        void getDocumentByOIBFromDB(String OIB);
    }

    interface View extends BaseView<Presenter>{

        void setDataToView(ScannedDocumentEntity scannedDocumentEntity);
        void getDocument(ScannedDocumentEntity scannedDocumentEntity);
    }
}
