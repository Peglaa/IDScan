package com.damir.stipancic.blinkstipancic.presenters;

import com.damir.stipancic.blinkstipancic.BlinkApplication;
import com.damir.stipancic.blinkstipancic.adapter.MainActivityRecyclerAdapter;
import com.damir.stipancic.blinkstipancic.adapter.MainActivityRecyclerViewHolder;
import com.damir.stipancic.blinkstipancic.contract.Contract;
import com.damir.stipancic.blinkstipancic.data.local.ScannedDocumentEntity;
import com.damir.stipancic.blinkstipancic.data.repository.ScannedDocumentRepository;
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer;

import java.util.ArrayList;
import java.util.List;

public class MainActivityPresenter implements Contract.Presenter.MainActivityPresenter, Contract.Repository.OnFinishedListener {

    private ScannedDocumentRepository mScannedDocumentRepository;
    private Contract.View.MainActivityView mMainActivityView;
    private final List<ScannedDocumentEntity> mScannedDocuments; //List used by RecyclerView

    public MainActivityPresenter(Contract.View.MainActivityView mainView) {
        this.mMainActivityView = mainView;
        this.mScannedDocumentRepository = new ScannedDocumentRepository(BlinkApplication.getAppContext());
        this.mScannedDocuments = new ArrayList<>();
    }


    @Override
    public void insertDocumentToDB(BlinkIdCombinedRecognizer.Result result) {

        mScannedDocumentRepository.insertDataToDB(result, BlinkApplication.getAppContext(), this);

    }

    @Override
    public void getScannedDocumentListFromDB() {
        mScannedDocumentRepository.getScannedDocumentListFromDB(this);
    }

    @Override
    public void onBindArticleData(MainActivityRecyclerViewHolder holder, int position) {
        ScannedDocumentEntity scannedDocumentEntity = mScannedDocuments.get(position);
        holder.setTitle(scannedDocumentEntity.getOIB());
    }

    @Override
    public int getDocumentCount() {
        return mScannedDocuments.size();
    }

    public String getOIB(int position){
        if(!mScannedDocuments.isEmpty())
            return mScannedDocuments.get(position).getOIB();
        return null;
    }

    @Override
    public void onFinished(List<ScannedDocumentEntity> scannedDocumentEntities) {
        if(!mScannedDocuments.isEmpty())
            mScannedDocuments.clear();

        mScannedDocuments.addAll(scannedDocumentEntities);
        mMainActivityView.setDataToRecyclerView();

    }

    @Override
    public void onFailure(Throwable t) {

    }
}
