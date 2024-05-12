package com.feng.freader.constract;

import com.feng.freader.entity.data.DiscoveryNovelData;

import java.util.List;

public interface IPressContract {
    interface View {
        void getCategoryNovelsSuccess(List<DiscoveryNovelData> dataList);
        void getCategoryNovelsError(String errorMsg);
    }
    interface Presenter {
        void getCategoryNovelsSuccess(List<DiscoveryNovelData> dataList);
        void getCategoryNovelsError(String errorMsg);

        void getCategoryNovels();   // 获取分类小说
    }
    interface Model {
        void getCategoryNovels();   // 获取分类小说
    }
}
