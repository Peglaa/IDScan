package com.damir.stipancic.blinkstipancic.contract;

import android.content.Context;

import com.damir.stipancic.blinkstipancic.adapter.MainActivityRecyclerViewHolder;
import com.damir.stipancic.blinkstipancic.data.local.ScannedDocumentEntity;
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer;

import java.util.List;

public interface Contract {

    interface Repository {

        interface RepositoryOnFinishedListener{

            void onFinished(List<ScannedDocumentEntity> scannedDocumentEntities);
            void onFailure(Throwable t);
        }

        void insertDataToDB(BlinkIdCombinedRecognizer.Result result, Context context, RepositoryOnFinishedListener onFinishedListener);
        void getScannedDocumentListFromDB(RepositoryOnFinishedListener onFinishedListener);

    }

    interface Presenter {

        interface PresenterOnFinishedListener{

            void onFinished(ScannedDocumentEntity scannedDocumentEntity);
            void onFailure(Throwable t);
        }

        interface MainActivityPresenter {

            void insertDocumentToDB(BlinkIdCombinedRecognizer.Result result);
            void getScannedDocumentListFromDB();
            void onBindArticleData(MainActivityRecyclerViewHolder holder, int position);
            int getDocumentCount();
        }

        interface DocumentInfoActivityPresenter {

            void getDocumentByOIBFromDB(String OIB);

        }
    }

    interface View {

        interface MainActivityView {

            void setDataToRecyclerView();
            void onResponseFailure(Throwable t);
            void updateRecyclerData();

            interface itemView{

                void setTitle(String title);
            }

        }

        interface DocumentInfoActivityView {

            void setDataToView(ScannedDocumentEntity scannedDocumentEntity);

            void getDocument(ScannedDocumentEntity scannedDocumentEntity);
        }
    }
}
