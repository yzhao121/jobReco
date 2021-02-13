package com.ita.job.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RegisterRequestBody {

    @JsonProperty("user_id")
    public String userId;

    public String password;

    @JsonProperty("first_name")
    public String firstname;
    @JsonProperty("last_name")
    public String lastname;
}
