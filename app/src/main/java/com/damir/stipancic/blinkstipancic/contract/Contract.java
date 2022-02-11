package com.damir.stipancic.blinkstipancic.contract;

import android.content.Context;

import com.damir.stipancic.blinkstipancic.adapter.MainRecyclerViewHolder;
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
            void onBindArticleData(MainRecyclerViewHolder holder, int position);
            int getDocumentCount();
        }

        interface DocumentInfoActivityPresenter {

            void getDocumentByIDFromDB(int ID);

        }
    }

    interface View {

        interface MainActivityView {

            void setDataToRecyclerView();

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
