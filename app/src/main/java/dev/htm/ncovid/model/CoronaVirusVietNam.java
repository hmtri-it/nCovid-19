package dev.htm.ncovid.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

import lombok.Data;

@Data
public class CoronaVirusVietNam implements Serializable {

    @SerializedName("recovered")
    @Expose
    private String recovered;
    @SerializedName("doubt")
    @Expose
    private String doubt;
    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("Lat")
    @Expose
    private String lat;
    @SerializedName("number")
    @Expose
    private String number;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("Lng")
    @Expose
    private String lng;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("deaths")
    @Expose
    private String deaths;

    public CoronaVirusVietNam(String recovered, String doubt, String date, String address, String deaths, String number) {
        this.recovered = recovered;
        this.doubt = doubt;
        this.date = date;
        this.address = address;
        this.deaths = deaths;
        this.number = number;
    }
}
