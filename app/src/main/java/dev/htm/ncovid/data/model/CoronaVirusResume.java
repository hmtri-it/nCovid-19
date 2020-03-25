package dev.htm.ncovid.data.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class CoronaVirusResume implements Serializable {
    private long cases;
    private long deaths;
    private long recovered;
    private long updated;

    public CoronaVirusResume(long cases, long deaths, long recovered, long updated) {
        this.cases = cases;
        this.deaths = deaths;
        this.recovered = recovered;
        this.updated = updated;
    }
}
