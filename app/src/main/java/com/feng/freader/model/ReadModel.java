package com.feng.freader.model;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.feng.freader.constant.Constant;
import com.feng.freader.constract.IReadContract;
import com.feng.freader.entity.epub.EpubData;
import com.feng.freader.entity.epub.OpfData;
import com.feng.freader.util.EpubUtils;
import com.google.gson.Gson;

import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadModel implements IReadContract.Model {
    private static final String TAG = "ReadModel";

    private IReadContract.Presenter mPresenter;
    private Gson mGson;

    private OpfData mOpfData;

    public ReadModel(IReadContract.Presenter mPresenter) {
        this.mPresenter = mPresenter;
        mGson = new Gson();
    }

    @Override
    public void getChapterList(String url) {
    }

    @Override
    public void getDetailedChapterData(String url) {
    }


    /**
     * 从 epub 文件中读取到 Opf 文件信息
     *
     * @param filePath epub 路径
     */
    @Override
    public void getOpfData(final String filePath) {
        File file = new File(filePath);
        final String savePath = Constant.EPUB_SAVE_PATH + "/" + file.getName();
        File saveFile = new File(savePath);
        if (saveFile.exists()) {
            getOpfDataImpl(savePath);
            if (mOpfData != null) {
                getOpfDataSuccess(mOpfData);
            }
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    EpubUtils.unZip(filePath, savePath);
                } catch (IOException e) {
                    e.printStackTrace();
                    getOpfDataError("解压失败，可能文件被加密");
                    return;
                }
                getOpfDataImpl(savePath);
                if (mOpfData != null) {
                    getOpfDataSuccess(mOpfData);
                }
            }
        }).start();
    }

    private void getOpfDataImpl(String savePath) {
        try {
            // 先得到 opf 文件的位置
            String opfPath = EpubUtils.getOpfPath(savePath);
            Log.d(TAG, "unZipEpub: opfPath = " + opfPath);
            // 解析 opf 文件
            mOpfData = EpubUtils.parseOpf(opfPath);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            getOpfDataError("Xml 解析出错");
        } catch (IOException e) {
            e.printStackTrace();
            getOpfDataError("I/O 错误");
        }
    }

    private void getOpfDataError(final String errorMsg) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mPresenter.getOpfDataError(errorMsg);
            }
        });
    }

    private void getOpfDataSuccess(final OpfData opfData) {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mPresenter.getOpfDataSuccess(opfData);
            }
        });
    }

    /**
     * 解析 html/xhtml 文件，得到 Epub 章节数据
     *
     * @param filePath 读取的文件路径
     */
    @Override
    public void getEpubChapterData(String parentPath, String filePath) {
        Log.d(TAG, "getEpubChapterData: filePath = " + filePath);
        List<EpubData> dataList = new ArrayList<>();
        try {
            dataList = EpubUtils.getEpubData(parentPath, filePath);
        } catch (IOException e) {
            e.printStackTrace();
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    mPresenter.getEpubChapterDataError("解析 html/xhtml：I/O 错误");
                }
            });
        }

        final List<EpubData> finalDataList = dataList;
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                mPresenter.getEpubChapterDataSuccess(finalDataList);
            }
        });
    }
}
