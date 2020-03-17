package dev.htm.ncovid.model;

import java.io.Serializable;

import lombok.Data;

@Data
public class TotalCase implements Serializable {
    private long cases;
    private long deaths;
    private long recovered;
    private long updated;
}
