package com.api.monitor.service;

import com.api.monitor.model.AggregateResponse;
import com.api.monitor.model.RequestLog;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;

public class RequestLogService {
    private static RequestLogService single_instance = null;

    private Vector<RequestLog> requestLogs;

    private RequestLogService() {
        this.requestLogs = new Vector<>();
    }

    public static RequestLogService getInstance() {
        if (single_instance == null) {
            single_instance = new RequestLogService();
        }
        return single_instance;
    }

    public void addRequestLogService(RequestLog log) {
        if (!(log.getUri().contains("WEB-INF") || log.getUri().contains("webjars"))) {
            log.setId(new java.util.Date().getTime() + "_" + Thread.currentThread().getId());
            this.requestLogs.add(log);
        }
    }

    public List<AggregateResponse> getAggregation() {
        List<AggregateResponse> aggregateResponses = new ArrayList<AggregateResponse>();
        Map<String, List<RequestLog>> groupByRequestLogs = this.requestLogs.stream()
                .collect(Collectors.groupingBy(RequestLog::getUri));

        for (Map.Entry<String, List<RequestLog>> entry: groupByRequestLogs.entrySet()) {
            long requestTimeMin = entry.getValue().stream().mapToLong(RequestLog::getRequestCompletionTime).min().orElse(0);
            long requestTimeMax = entry.getValue().stream().mapToLong(RequestLog::getRequestCompletionTime).max().orElse(0);
            double requestTimeAvg = entry.getValue().stream().mapToLong(RequestLog::getRequestCompletionTime).average().orElse(0.00);
            BigDecimal bdRtAvg = BigDecimal.valueOf(requestTimeAvg);
            bdRtAvg = bdRtAvg.setScale(2, RoundingMode.HALF_UP);

            long responseSizeMin = entry.getValue().stream().mapToLong(RequestLog::getContentSize).min().orElse(0);
            long responseSizeMax = entry.getValue().stream().mapToLong(RequestLog::getContentSize).max().orElse(0);
            double responseSizeAvg = entry.getValue().stream().mapToLong(RequestLog::getContentSize).average().orElse(0.00);
            BigDecimal bdRsAvg = BigDecimal.valueOf(responseSizeAvg);
            bdRsAvg = bdRsAvg.setScale(2, RoundingMode.HALF_UP);

            AggregateResponse aggregateResponse = new AggregateResponse();
            aggregateResponse.setUri(entry.getValue().get(0).getUri());
            aggregateResponse.setHttpMethod(entry.getValue().get(0).getHttpMethod());
            aggregateResponse.setRequestTimeAvg(bdRtAvg.doubleValue());
            aggregateResponse.setRequestTimeMax(requestTimeMax);
            aggregateResponse.setRequestTimeMin(requestTimeMin);
            aggregateResponse.setResponseSizeAvg(bdRsAvg.doubleValue());
            aggregateResponse.setResponseSizeMax(responseSizeMax);
            aggregateResponse.setResponseSizeMin(responseSizeMin);
            aggregateResponses.add(aggregateResponse);
        }
        return aggregateResponses;
    }

    public List<RequestLog> getRequestLogs(String uri) {
        return this.requestLogs.stream().filter(log -> log.getUri().equalsIgnoreCase(uri)).collect(Collectors.toList());
    }
}
