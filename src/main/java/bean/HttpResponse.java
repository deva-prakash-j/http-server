package bean;

import java.nio.charset.StandardCharsets;

public class HttpResponse {

    private String httpVersion = "HTTP/1.1";
    private int statusCode;
    private String statusText;
    private String contentType;
    private int contentLength;
    private String body;

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public byte[] getBytes() {
        StringBuilder builder = new StringBuilder();
        builder.append(httpVersion).append(' ')
                .append(statusCode).append(' ')
                .append(statusText)
                .append("\r\n");

        if (contentType != null) {
            builder.append("Content-Type: ").append(contentType).append("\r\n");
        }
        if (contentLength > 0) {
            builder.append("Content-Length: ").append(contentLength).append("\r\n");
        }
        builder.append("\r\n");
        if(body != null) {
            builder.append(body);
        }
        return builder.toString().getBytes(StandardCharsets.UTF_8);
    }
}
