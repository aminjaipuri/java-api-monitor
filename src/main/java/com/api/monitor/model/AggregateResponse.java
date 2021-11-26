package com.api.monitor.model;

public class AggregateResponse {
    private String uri;
    private String httpMethod;
    private long requestTimeMax;
    private long requestTimeMin;
    private double requestTimeAvg;

    private long responseSizeMax;
    private long responseSizeMin;
    private double responseSizeAvg;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public void setHttpMethod(String httpMethod) {
        this.httpMethod = httpMethod;
    }

    public long getRequestTimeMax() {
        return requestTimeMax;
    }

    public void setRequestTimeMax(long requestTimeMax) {
        this.requestTimeMax = requestTimeMax;
    }

    public long getRequestTimeMin() {
        return requestTimeMin;
    }

    public void setRequestTimeMin(long requestTimeMin) {
        this.requestTimeMin = requestTimeMin;
    }

    public double getRequestTimeAvg() {
        return requestTimeAvg;
    }

    public void setRequestTimeAvg(double requestTimeAvg) {
        this.requestTimeAvg = requestTimeAvg;
    }

    public long getResponseSizeMax() {
        return responseSizeMax;
    }

    public void setResponseSizeMax(long responseSizeMax) {
        this.responseSizeMax = responseSizeMax;
    }

    public long getResponseSizeMin() {
        return responseSizeMin;
    }

    public void setResponseSizeMin(long responseSizeMin) {
        this.responseSizeMin = responseSizeMin;
    }

    public double getResponseSizeAvg() {
        return responseSizeAvg;
    }

    public void setResponseSizeAvg(double responseSizeAvg) {
        this.responseSizeAvg = responseSizeAvg;
    }
}
