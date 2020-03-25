package dev.htm.ncovid.data.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class CoronaVirus implements Serializable {

    private String country;
    private String cases;
    private String todayCases;
    private String deaths;
    private String todayDeaths;
    private String recovered;
    private String active;
    private String critical;
    private String casesPerOneMillion;

    public CoronaVirus(String country, String cases, String todayCases, String deaths, String todayDeaths, String recovered, String active, String critical, String casesPerOneMillion) {
        this.country = country;
        this.cases = cases;
        this.todayCases = todayCases;
        this.deaths = deaths;
        this.todayDeaths = todayDeaths;
        this.recovered = recovered;
        this.active = active;
        this.critical = critical;
        this.casesPerOneMillion = casesPerOneMillion;
    }

}
