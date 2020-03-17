package dev.htm.ncovid.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class NCovidLiveData implements Serializable {

    private String country;
    private long cases;
    private long todayCases;
    private long deaths;
    private long todayDeaths;
    private long recovered;
    private long critical;
}
