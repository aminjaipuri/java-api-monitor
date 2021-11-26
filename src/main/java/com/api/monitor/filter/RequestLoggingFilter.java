package com.api.monitor.filter;

import com.api.monitor.model.RequestLog;
import com.api.monitor.service.RequestLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Component
@Order (1)
public class RequestLoggingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Autowired
    private RequestLogService requestLogService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //System.out.println("RequestLoggingFilter doFilter");
        Date before = new java.util.Date();
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        //System.out.println("Before RequestURL: " +httpRequest.getRequestURI() );

        ResponseLogWrapper responseWrapper = new ResponseLogWrapper(httpResponse);

        chain.doFilter(request, responseWrapper);
        //System.out.println("After RequestURL: " +httpRequest.getRequestURI() );

        long contentSize = 0;
        if (!(responseWrapper.getResponse() instanceof ResponseLogWrapper)) {
            //System.out.println(httpRequest.getRequestURI() + ", len: " + responseWrapper.getContent() != null ? responseWrapper.getContent().getBytes().length : 0);
            contentSize = responseWrapper.getContent() != null ? responseWrapper.getContent().getBytes().length : 0;
        }

        long after= System.currentTimeMillis();
        long totalTime = (after - before.getTime());
        //System.out.println("Total time for the RequestURL: " +httpRequest.getRequestURI() +", is : " +totalTime  + " ms");
        requestLogService.addRequestLogService(new RequestLog(httpRequest.getRequestURI(),httpRequest.getMethod(), before, totalTime, contentSize));
    }

    @Override
    public void destroy() {}
}


