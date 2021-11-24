package com.example.hanut.models;

import java.util.ArrayList;

public class FCMResponse {

    private long multicas_id;
    private int success;
    private int failure;
    private int canonical_ids;
    ArrayList<Object> results = new ArrayList<Object>();

    public FCMResponse(long multicas_id, int success, int failure, int canonical_ids, ArrayList<Object> results) {
        this.multicas_id = multicas_id;
        this.success = success;
        this.failure = failure;
        this.canonical_ids = canonical_ids;
        this.results = results;
    }

    public long getMulticas_id() {
        return multicas_id;
    }

    public void setMulticas_id(long multicas_id) {
        this.multicas_id = multicas_id;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public int getFailure() {
        return failure;
    }

    public void setFailure(int failure) {
        this.failure = failure;
    }

    public int getCanonical_ids() {
        return canonical_ids;
    }

    public void setCanonical_ids(int canonical_ids) {
        this.canonical_ids = canonical_ids;
    }

    public ArrayList<Object> getResults() {
        return results;
    }

    public void setResults(ArrayList<Object> results) {
        this.results = results;
    }
}
