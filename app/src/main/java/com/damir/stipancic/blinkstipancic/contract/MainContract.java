package com.damir.stipancic.blinkstipancic.contract;

import com.damir.stipancic.blinkstipancic.adapter.MainRecyclerViewHolder;
import com.damir.stipancic.blinkstipancic.data.local.ScannedDocumentEntity;
import com.damir.stipancic.blinkstipancic.presenters.BasePresenter;
import com.damir.stipancic.blinkstipancic.view.BaseView;
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer;

import java.util.List;

public interface MainContract {
    interface Presenter extends BasePresenter{

        interface OnFinishedListener {
            void onFinished(List<ScannedDocumentEntity> scannedDocumentEntities);
            void onFailure(Throwable t);
        }

        void onViewCreated();
        void insertDataToDB(BlinkIdCombinedRecognizer.Result result);
        void getData();
        int getDocumentCount();
        int getId(int position);
        void onBindDocumentData(MainRecyclerViewHolder holder, int position);
    }

    interface View extends BaseView<Presenter>{
        void setDataToRecyclerView();

        interface itemView{
            void setTitle(int title);
        }
    }
}
