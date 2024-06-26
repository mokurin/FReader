package com.feng.freader.constract;

import com.feng.freader.entity.data.CatalogData;

public interface ICatalogContract {
    interface View {
        void getCatalogDataSuccess(CatalogData catalogData);
        void getCatalogDataError(String errorMsg);
    }
    interface Presenter {
        void getCatalogDataSuccess(CatalogData catalogData);
        void getCatalogDataError(String errorMsg);

        void getCatalogData(String url);
    }
    interface Model {
        void getCatalogData(String url);    // 获取目录信息
    }
}
