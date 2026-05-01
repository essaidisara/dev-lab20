package com.example.numberbookess;

import com.google.gson.annotations.SerializedName;


public class ApiResponseEss {

    private boolean success;
    private String message;

    @SerializedName("signature")
    private String apiSignature;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public String getApiSignature() {
        return apiSignature;
    }
}