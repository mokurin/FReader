package com.feng.freader.base;

public class BasePresenter<V> {
    private V view;

    public void attachView(V view){
        this.view = view;
    }

    public void detachView(){
        this.view = null;
    }

    protected  boolean isAttachView(){
        return view != null;
    }

    protected V getMvpView(){
        return view;
    }
}
