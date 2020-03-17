package dev.htm.ncovid.service;

public interface OnCallback {
    void onSuccess(String type, String data);

    void onError(String error);
}
