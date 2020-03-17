package dev.htm.ncovid.service;

public interface OnCallback {
    void onSuccess(String data);

    void onError(String error);
}
