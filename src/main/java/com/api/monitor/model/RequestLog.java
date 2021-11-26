package com.api.monitor.model;

import java.util.Date;

public class RequestLog {
    private String id;
    private String uri;
    private String httpMethod;
    private Date requestedTime;
    private long requestCompletionTime;
    private long contentSize;

    public RequestLog(String uri, String httpMethod, Date requestedTime, long requestCompletionTime, long contentSize) {
        this.uri = uri;
        this.httpMethod = httpMethod;
        this.requestedTime = requestedTime;
        this.requestCompletionTime = requestCompletionTime;
        this.contentSize = contentSize;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
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
    public Date getRequestedTime() { return requestedTime; }
    public void setRequestedTime(Date requestedTime) { this.requestedTime = requestedTime; }
    public long getRequestCompletionTime() { return requestCompletionTime; }
    public void setRequestCompletionTime(long requestCompletionTime) { this.requestCompletionTime = requestCompletionTime; }
    public long getContentSize() { return contentSize; }
    public void setContentSize(long contentSize) { this.contentSize = contentSize; }
}
