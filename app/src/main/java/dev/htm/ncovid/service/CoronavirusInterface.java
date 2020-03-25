package dev.htm.ncovid.service;

import java.util.List;

import dev.htm.ncovid.data.model.CoronaVirus;
import dev.htm.ncovid.data.model.CoronaVirusResume;
import dev.htm.ncovid.data.model.CoronaVirusVietNam;
import retrofit2.Call;
import retrofit2.http.GET;

public interface CoronavirusInterface {

    @GET("all")
    public Call<CoronaVirusResume> getCoronaVirusTotalWorldWide();

    @GET("countries")
    public Call<List<CoronaVirus>> getCoronaVirusCompleteInformation();

    @GET("countries/vietnam")
    public Call<CoronaVirus> getCoronaVirusTotalVietName();

    @GET("https://maps.vnpost.vn/app/api/democoronas/")
    public Call<List<CoronaVirusVietNam>> getCoronaVirusVietNameInformation();
}
