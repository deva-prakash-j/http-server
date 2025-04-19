package util;

import bean.HttpRequest;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class RequestUtil {

    public static HttpRequest getHttpRequest(InputStream inputStream) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(inputStream);

        byte[] bytes = new byte[1024];
        int bytesRead = 0;
        int b;
        int methodEnd = -1;
        int pathEnd = -1;
        int requestLineEnd = -1;

        while((b = bis.read()) != -1) {
            bytes[bytesRead++] = (byte) b;

            if(b == ' ' && methodEnd == -1) {
                methodEnd = bytesRead - 1;
            } else if(b == ' ' && pathEnd == -1) {
                pathEnd = bytesRead - 1;
            } else if(b == '\n' && bytesRead >= 2 && bytes[bytesRead - 2] == '\r') {
                requestLineEnd = bytesRead;
                break;
            }

            if(bytesRead >= bytes.length) {
                throw new IOException("Request line too long");
            }
        }

        if(methodEnd == -1 || pathEnd == -1 || requestLineEnd == -1) {
            throw new IOException("Malformed request");
        }


        return new HttpRequest(
                new String(bytes, 0, methodEnd, StandardCharsets.US_ASCII),
                new String(bytes, methodEnd + 1, pathEnd - methodEnd - 1, StandardCharsets.US_ASCII),
                new String(bytes, pathEnd + 1, requestLineEnd - pathEnd - 3, StandardCharsets.US_ASCII));
    }
}
