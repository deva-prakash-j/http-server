package bean;

import util.RequestUtil;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class HttpRequest{
    public final String method;
    public final String path;
    public final String httpVersion;
    private Map<String, String> headers = null;
    private final BufferedInputStream bufferedInput;
    private String body;

    public HttpRequest(String method, String path, String httpVersion, BufferedInputStream bufferedInput) {
        this.method = method;
        this.path = path;
        this.httpVersion = httpVersion;
        this.bufferedInput = bufferedInput;
    }

    public Map<String, String> getHeaders() {
        if(headers == null) {
            try {
                this.headers = RequestUtil.parseHeaders(bufferedInput);
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return headers;
    }

    public String getBody() {
        if(body == null) {
            try {
                body = RequestUtil.parseBody(bufferedInput, getHeaders());
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return body;
    }


}
