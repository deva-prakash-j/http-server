package bean;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;

import static constants.ServerConstants.*;
import static constants.ServerConstants.CONTENT_LENGTH;

public class HttpResponse {

    private String httpVersion = "HTTP/1.1";
    private int statusCode;
    private String statusText;
    private Map<String, String> headers = new HashMap<>();
    private String body;
    private byte[] bodyBytes;

    public byte[] getBodyBytes() {
        return bodyBytes;
    }

    public boolean isEncoded = false;

    public HttpResponse setStatusCode(int statusCode) {
        this.statusCode = statusCode;
        return this;
    }

    public HttpResponse setStatusText(String statusText) {
        this.statusText = statusText;
        return this;
    }

    public HttpResponse addHeader(String name, String value) {
        headers.put(name, value);
        return this;
    }

    public HttpResponse setBody(String body) {
        this.body = body;
        return this;
    }

    public byte[] getBytes(String connection) {
        int contentLength = 0;
        StringBuilder builder = new StringBuilder();
        builder.append(httpVersion).append(' ')
                .append(statusCode).append(' ')
                .append(statusText)
                .append("\r\n");

        for(Map.Entry<String, String> entry : headers.entrySet()) {
            if(CONTENT_ENCODING.equalsIgnoreCase(entry.getKey()) && entry.getValue() != null) {
                if(entry.getValue().toLowerCase().contains(SUPPORTED_ENCODING)) {
                    builder.append(entry.getKey()).append(": ").append(SUPPORTED_ENCODING).append("\r\n");
                    this.bodyBytes = compressGzip(body);
                    if(bodyBytes != null) {
                        contentLength = bodyBytes.length;
                    }
                    this.isEncoded = true;
                }
            } else if(CONTENT_LENGTH.equalsIgnoreCase(entry.getKey()) && entry.getValue() != null) {
                contentLength = contentLength == 0 ? Integer.parseInt(entry.getValue()) : contentLength;
            } else {
                builder.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
            }
        }

        if(contentLength != 0) {
            builder.append(CONTENT_LENGTH).append(": ").append(contentLength).append("\r\n");
        }

        builder.append(CONNECTION).append(": ");
        if(connection != null) {
            builder.append(connection);
        } else {
            builder.append(KEEP_ALIVE);
        }
        builder.append("\r\n");

        builder.append("\r\n");
        if(body != null && !isEncoded) {
            builder.append(body);
        }

        return builder.toString().getBytes(StandardCharsets.UTF_8);
    }

    public static byte[] compressGzip(String data) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try (GZIPOutputStream gzipStream = new GZIPOutputStream(byteArrayOutputStream)) {
                gzipStream.write(data.getBytes(StandardCharsets.UTF_8));
            }
            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
