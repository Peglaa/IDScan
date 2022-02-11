package com.damir.stipancic.blinkstipancic.presenters;

import com.damir.stipancic.blinkstipancic.BlinkApplication;
import com.damir.stipancic.blinkstipancic.DI.DependencyInjector;
import com.damir.stipancic.blinkstipancic.adapter.MainRecyclerViewHolder;
import com.damir.stipancic.blinkstipancic.contract.MainContract;
import com.damir.stipancic.blinkstipancic.data.local.ScannedDocumentEntity;
import com.damir.stipancic.blinkstipancic.data.repository.ScannedDocumentRepository;
import com.microblink.entities.recognizers.blinkid.generic.BlinkIdCombinedRecognizer;

import java.util.ArrayList;
import java.util.List;

public class MainPresenter implements MainContract.Presenter, MainContract.Presenter.OnFinishedListener {

    private MainContract.View mView;
    private final ScannedDocumentRepository mRepository;
    private final List<ScannedDocumentEntity> mScannedDocuments; //List used by RecyclerView

    public MainPresenter(MainContract.View view, DependencyInjector dependencyInjector) {
        this.mView = view;
        this.mRepository = dependencyInjector.scannedDocumentRepository();
        this.mScannedDocuments = new ArrayList<>();
    }

    @Override
    public void onViewCreated() {
        getData();
    }

    @Override
    public void insertDataToDB(BlinkIdCombinedRecognizer.Result result) {
        mRepository.insertDataToDB(result, BlinkApplication.getAppContext(), this);
    }

    @Override
    public void getData() {
        mRepository.getScannedDocumentListFromDB(this);
    }

    @Override
    public int getDocumentCount() {
        return mScannedDocuments.size();
    }

    @Override
    public int getId(int position) {
        if(!mScannedDocuments.isEmpty())
            return mScannedDocuments.get(position).getId();
        return -1;
    }

    @Override
    public void onBindDocumentData(MainRecyclerViewHolder holder, int position) {
        ScannedDocumentEntity scannedDocumentEntity = mScannedDocuments.get(position);
        holder.setTitle(scannedDocumentEntity.getId());
    }

    @Override
    public void onDestroy() {
        this.mView = null;
    }

    @Override
    public void onFinished(List<ScannedDocumentEntity> scannedDocumentEntities) {
        if(!mScannedDocuments.isEmpty())
            mScannedDocuments.clear();

        mScannedDocuments.addAll(scannedDocumentEntities);
        mView.setDataToRecyclerView();
    }

    @Override
    public void onFailure(Throwable t) {

    }
}
