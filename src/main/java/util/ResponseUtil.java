package util;

import bean.HttpResponse;

import static constants.ServerConstants.*;

public class ResponseUtil {

    public static Handler getMethodNotSupportedHandler() {
        return (request, param) -> {
            return new HttpResponse()
                    .setStatusCode(NOT_ALLOWED_STATUS_CODE).setStatusText(NOT_ALLOWED_STATUS_TEXT);
        };
    }

    public static Handler getInternalServerErrorHandler() {
        return (request, param) -> {
            return new HttpResponse()
                    .setStatusCode(ERROR_STATUS_CODE).setStatusText(ERROR_STATUS_TEXT);
        };
    }
}
