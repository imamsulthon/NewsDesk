package com.imams.newsdesk.model.response;

import com.google.gson.annotations.SerializedName;
import com.imams.newsdesk.model.Source;

import java.util.ArrayList;

public class SourceResponse {

    @SerializedName("status")
    private String status;
    @SerializedName("sources")
    private ArrayList<Source> sources;

    public SourceResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<Source> getSources() {
        return sources;
    }

    public void setSources(ArrayList<Source> sources) {
        this.sources = sources;
    }
}
