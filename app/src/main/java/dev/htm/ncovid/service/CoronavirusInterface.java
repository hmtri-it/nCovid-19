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

    @GET("countries/Viet%20Nam")
    public Call<CoronaVirus> getCoronaVirusTotalVietName();

    @GET("<??>")
    public Call<List<CoronaVirusVietNam>> getCoronaVirusVietNameInformation();
}
