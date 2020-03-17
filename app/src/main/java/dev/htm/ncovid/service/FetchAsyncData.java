package dev.htm.ncovid.service;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static dev.htm.ncovid.BuildConfig.BASE_URL;

/**
 * API nCoVid-19
 * Base url: https://corona.lmao.ninja/
 * Get the endpoint https://corona.lmao.ninja/all to get information for all cases
 * get the endpoint https://corona.lmao.ninja/countries for getting the data sorted country wise
 * get the endpoint https://corona.lmao.ninja/countries/[country-name] for getting the data for a specific country
 */
public class FetchAsyncData extends AsyncTask<String, Void, String> {

    private static final String TAG = FetchAsyncData.class.getSimpleName();

    private Context context;
    private String endpoint;
    private OnCallback callback;

    public static final String ALL = "all";
    public static final String COUNTRIES = "countries";
    public final String COUNTRIES_NAME = "countries/";

    public FetchAsyncData(Context context, String endpoint, OnCallback callback) {
        this.context = context;
        this.endpoint = endpoint;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... strings) {
        String result = null;
        try {
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(BASE_URL + endpoint)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                result = response.body().string();
                return result;
            }
        } catch (IOException e) {
            e.printStackTrace();
            callback.onError(e.getMessage());
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        super.onPostExecute(result);
        if (result != null) {
            callback.onSuccess(result);
        }
    }
}
