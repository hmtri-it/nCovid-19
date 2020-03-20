package dev.htm.ncovid.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import dev.htm.ncovid.model.CoronaVirus;
import dev.htm.ncovid.model.CoronaVirusResume;
import dev.htm.ncovid.service.CoronaVirusClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoronaVirusViewModel extends ViewModel {

    public MutableLiveData<CoronaVirusResume> mutableResumeLiveData = new MutableLiveData<>();
    public MutableLiveData<List<CoronaVirus>> mutableCompleteLiveData = new MutableLiveData<>();

    public MutableLiveData<CoronaVirusResume> getMutableResumeLiveData() {
        return mutableResumeLiveData;
    }

    public void getCoronaResumeInformation() {
        CoronaVirusClient.getInstance().getCoronaVirusTotalWorldWide().enqueue(new Callback<CoronaVirusResume>() {
            @Override
            public void onResponse(@NonNull Call<CoronaVirusResume> call, @NonNull Response<CoronaVirusResume> response) {
                if (response.body() != null) {
                    long cases = response.body().getCases();
                    long deaths = response.body().getDeaths();
                    long recovered = response.body().getRecovered();
                    long updated = response.body().getUpdated();
                    mutableResumeLiveData.setValue(new CoronaVirusResume(cases, deaths, recovered, updated));
                }
            }

            @Override
            public void onFailure(@NonNull Call<CoronaVirusResume> call, @NonNull Throwable t) {

            }
        });

    }

    public void getCoronaCompleteInformation() {
        List<CoronaVirus> coronaVirusDatas = new ArrayList<>();
        CoronaVirusClient.getInstance().getCoronaVirusCompleteInformation().enqueue(new Callback<List<CoronaVirus>>() {
            @Override
            public void onResponse(@NonNull Call<List<CoronaVirus>> call, @NonNull Response<List<CoronaVirus>> response) {
                if (response.body() != null) {
                    for (int i = 0; i < response.body().size(); i++) {
                        coronaVirusDatas.add(new CoronaVirus(
                                response.body().get(i).getCountry(),
                                response.body().get(i).getCases(),
                                response.body().get(i).getTodayCases(),
                                response.body().get(i).getDeaths(),
                                response.body().get(i).getTodayDeaths(),
                                response.body().get(i).getRecovered(),
                                response.body().get(i).getActive(),
                                response.body().get(i).getCritical(),
                                response.body().get(i).getCasesPerOneMillion()
                        ));

                    }
                    mutableCompleteLiveData.setValue(coronaVirusDatas);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<CoronaVirus>> call, @NonNull Throwable t) {

            }
        });
    }
}
