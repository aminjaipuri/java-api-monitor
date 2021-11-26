package com.api.monitor.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.*;
import static java.nio.charset.StandardCharsets.UTF_8;

public class ResponseLogWrapper extends HttpServletResponseWrapper {
    /**
     * Constructs a response adaptor wrapping the given response.
     *
     * @param response The response to be wrapped
     * @throws IllegalArgumentException if the response is null
     */
    public ResponseLogWrapper(HttpServletResponse response) {
        super(response);
    }

    private ServletOutputStream outputStream;
    private PrintWriter writer;
    private ByteArrayOutputStream baos;

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called on this response.");
        }

        if (outputStream == null) {
            outputStream = getResponse().getOutputStream();
            this.baos = new ByteArrayOutputStream(1024);
        }
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (outputStream != null) {
            throw new IllegalStateException("getOutputStream() has already been called on this response.");
        }

        if (writer == null) {
            writer = new PrintWriter(new OutputStreamWriter(outputStream, getResponse().getCharacterEncoding()), true);
        }

        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (writer != null) {
            writer.flush();
        } else if (outputStream != null) {
            outputStream.flush();
        }
    }

    public String getContent() {
        try {
            flushBuffer();
            if (outputStream == null) {
                return null;
            }
            String responseEncoding = getResponse().getCharacterEncoding();
            return baos.toString(responseEncoding != null ? responseEncoding : UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return "[UNSUPPORTED ENCODING]";
        } catch (IOException e) {
            return "[IO EXCEPTION]";
        }
    }
}