package com.feng.freader.http;

public interface OkhttpCall {
    void onResponse(String json);
    void onFailure(String errorMsg);
}
