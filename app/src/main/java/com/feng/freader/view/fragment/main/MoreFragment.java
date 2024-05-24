package com.feng.freader.view.fragment.main;

import android.view.View;
import android.widget.TextView;

import com.feng.freader.R;
import com.feng.freader.base.BaseFragment;
import com.feng.freader.base.BasePresenter;
import com.feng.freader.constant.EventBusCode;
import com.feng.freader.entity.eventbus.Event;
import com.feng.freader.util.FileUtil;
import com.feng.freader.util.VersionUtil;
import com.feng.freader.widget.TipDialog;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class MoreFragment extends BaseFragment implements View.OnClickListener{
    private View mCheckUpdateV;
    private TextView mVersionTv;
    private View mClearV;
    private TextView mCacheSizeTv;

    @Override
    protected void doInOnCreate() {
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_more;
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void initView() {
        mCheckUpdateV = getActivity().findViewById(R.id.v_more_check_update);
        mCheckUpdateV.setOnClickListener(this);
        mVersionTv = getActivity().findViewById(R.id.tv_more_version);
        mVersionTv.setText(VersionUtil.getVersionName(getActivity()));

        mClearV = getActivity().findViewById(R.id.v_more_clear);
        mClearV.setOnClickListener(this);
        mCacheSizeTv = getActivity().findViewById(R.id.tv_more_cache_size);
        mCacheSizeTv.setText(FileUtil.getLocalCacheSize());
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected boolean isRegisterEventBus() {
        return true;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        switch (event.getCode()) {
            case EventBusCode.MORE_INTO:
                mCacheSizeTv.setText(FileUtil.getLocalCacheSize());
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_more_check_update:
                showShortToast("已经是最新版本");
                break;
            case R.id.v_more_clear:
                final TipDialog tipDialog = new TipDialog.Builder(getActivity())
                        .setContent("是否清除缓存")
                        .setCancel("否")
                        .setEnsure("是")
                        .setOnClickListener(new TipDialog.OnClickListener() {
                            @Override
                            public void clickEnsure() {
                                FileUtil.clearLocalCache();
                                mCacheSizeTv.setText(FileUtil.getLocalCacheSize());
                            }

                            @Override
                            public void clickCancel() {

                            }
                        })
                        .build();
                tipDialog.show();
                break;
            default:
                break;
        }
    }
}
