package dev.htm.ncovid.service;

import android.content.Context;
import android.os.AsyncTask;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static dev.htm.ncovid.BuildConfig.BASE_URL;

/**
 * API nCoVid-19
 * Base url: https://corona.lmao.ninja/
 * Get the endpoint https://corona.lmao.ninja/all to get information for all cases
 * get the endpoint https://corona.lmao.ninja/countries for getting the data sorted country wise
 * get the endpoint https://corona.lmao.ninja/countries/[country-country_name] for getting the data for a specific country
 * get the endpoint https://corona.lmao.ninja/countries?sort=[property]
 * for getting the data sorted country wise, sorting by property (ex. cases, todayCases, deaths, todayDeaths, recovered, critical)
 */
public class FetchAsyncData extends AsyncTask<String, Void, String> {

    public static final String ALL = "all";
    public static final String COUNTRIES = "countries";
    public static final String COUNTRIES_SORT = "countries?sort=";
    private static final String TAG = FetchAsyncData.class.getSimpleName();
    public final String COUNTRIES_NAME = "countries/";
    private Context context;
    private String endpoint;
    private OnCallback callback;
    private String type;

    public FetchAsyncData(Context context, String endpoint, OnCallback callback, String type) {
        this.context = context;
        this.endpoint = endpoint;
        this.callback = callback;
        this.type = type;
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
            callback.onSuccess(type, result);
        }
    }
}
