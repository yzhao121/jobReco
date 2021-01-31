package com.ita.job.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ExtractRequestBody {
    public List<String> data;

    @JsonProperty("max_keywords")
    public int maxKeywords;

    public ExtractRequestBody(List<String> data, int maxKeywords) {
        this.data = data;
        this.maxKeywords = maxKeywords;
    }
}
