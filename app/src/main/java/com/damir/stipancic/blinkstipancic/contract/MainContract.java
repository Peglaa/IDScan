package com.damir.stipancic.blinkstipancic.contract;

import com.damir.stipancic.blinkstipancic.adapter.MainRecyclerViewHolder;
import com.damir.stipancic.blinkstipancic.presenters.BasePresenter;
import com.damir.stipancic.blinkstipancic.view.BaseView;
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer;

public interface MainContract {
    interface Presenter extends BasePresenter{
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
