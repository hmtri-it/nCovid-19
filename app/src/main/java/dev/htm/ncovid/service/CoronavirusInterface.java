package dev.htm.ncovid.service;

import java.util.List;

import dev.htm.ncovid.model.CoronaVirus;
import dev.htm.ncovid.model.CoronaVirusResume;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CoronavirusInterface {

    @GET("all")
    public Call<CoronaVirusResume> getCoronaVirusTotalWorldWide();

    @GET("countries")
    public Call<List<CoronaVirus>> getCoronaVirusCompleteInformation();
}
