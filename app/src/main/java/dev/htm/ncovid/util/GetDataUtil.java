package dev.htm.ncovid.util;

import android.content.Context;

import dev.htm.ncovid.service.FetchAsyncData;
import dev.htm.ncovid.service.OnCallback;

public class GetDataUtil {
    public static void totalCase(Context context, String endpoint,  OnCallback callback) {
        fetchdata(context, endpoint, callback);
    }

    private static void fetchdata(Context context, String endpoint, OnCallback callback) {
        new FetchAsyncData(context, endpoint, callback).execute();
    }

}
