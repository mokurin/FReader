package com.feng.freader.presenter;

import com.feng.freader.base.BasePresenter;
import com.feng.freader.constract.ICatalogContract;
import com.feng.freader.entity.data.CatalogData;
import com.feng.freader.model.CatalogModel;

public class CatalogPresenter extends BasePresenter<ICatalogContract.View>
        implements ICatalogContract.Presenter {

    private ICatalogContract.Model mModel;

    public CatalogPresenter() {
        mModel = new CatalogModel(this);
    }

    @Override
    public void getCatalogDataSuccess(CatalogData catalogData) {
        if (isAttachView()) {
            getMvpView().getCatalogDataSuccess(catalogData);
        }
    }

    @Override
    public void getCatalogDataError(String errorMsg) {
        if (isAttachView()) {
            getMvpView().getCatalogDataError(errorMsg);
        }
    }

    @Override
    public void getCatalogData(String url) {
        mModel.getCatalogData(url);
    }
}
