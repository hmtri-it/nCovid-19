package dev.htm.ncovid.service;

import java.util.List;

import dev.htm.ncovid.BuildConfig;
import dev.htm.ncovid.model.CoronaVirus;
import dev.htm.ncovid.model.CoronaVirusResume;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CoronaVirusClient {
    private CoronavirusInterface mCoronaInterface;
    private static CoronaVirusClient mCoronaClient;

    public CoronaVirusClient() {
        Retrofit mRetrofit = new Retrofit.Builder().baseUrl(BuildConfig.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        mCoronaInterface = mRetrofit.create(CoronavirusInterface.class);
    }

    public static CoronaVirusClient getInstance() {
        if (mCoronaClient != null)
            return mCoronaClient;
        else {
            mCoronaClient = new CoronaVirusClient();
            return mCoronaClient;
        }
    }

    public Call<List<CoronaVirus>> getCoronaVirusCompleteInformation() {
        return mCoronaInterface.getCoronaVirusCompleteInformation();
    }

    public Call<CoronaVirusResume> getCoronaVirusTotalWorldWide() {
        return mCoronaInterface.getCoronaVirusTotalWorldWide();
    }
}
