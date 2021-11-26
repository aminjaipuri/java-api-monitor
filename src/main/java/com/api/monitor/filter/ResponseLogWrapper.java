package com.api.monitor.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
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
    private LoggingServletOutputStream logStream;
    private PrintWriter writer;


    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        if (writer != null) {
            throw new IllegalStateException("getWriter() has already been called on this response.");
        }

        if (outputStream == null) {
            outputStream = getResponse().getOutputStream();
            logStream = new LoggingServletOutputStream(outputStream);
        }

        return logStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (outputStream != null) {
            throw new IllegalStateException("getOutputStream() has already been called on this response.");
        }

        if (writer == null) {
            logStream = new LoggingServletOutputStream(getResponse().getOutputStream());
            writer = new PrintWriter(new OutputStreamWriter(logStream, getResponse().getCharacterEncoding()), true);
        }

        return writer;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (writer != null) {
            writer.flush();
        } else if (outputStream != null) {
            logStream.flush();
        }
    }

    public String getContent() {
        try {
            flushBuffer();
            if (logStream == null) {
                return null;
            }
            String responseEncoding = getResponse().getCharacterEncoding();
            return logStream.baos.toString(responseEncoding != null ? responseEncoding : UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return "[UNSUPPORTED ENCODING]";
        } catch (IOException e) {
            return "[IO EXCEPTION]";
        }
    }

    private class LoggingServletOutputStream extends ServletOutputStream {
        public LoggingServletOutputStream(ServletOutputStream outputStream) {

            this.outputStream = outputStream;
            this.baos = new ByteArrayOutputStream(1024);
        }

        private ServletOutputStream outputStream;
        private ByteArrayOutputStream baos;

        @Override
        public boolean isReady() {
            return outputStream.isReady();
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            outputStream.setWriteListener(writeListener);
        }

        @Override
        public void write(int b) throws IOException {
            outputStream.write(b);
            baos.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            outputStream.write(b);
            baos.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            outputStream.write(b, off, len);
            baos.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            outputStream.flush();
            baos.flush();
        }

        public void close() throws IOException {
            outputStream.close();
            baos.close();
        }
    }

}