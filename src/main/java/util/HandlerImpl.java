package util;

import bean.HttpResponse;

import static constants.ServerConstants.*;

public class HandlerImpl {

    public static Handler readFile(String directory) {
        return (request, params) -> {
            String content = RequestUtil.readFile(directory, params.get("fileName"));
            HttpResponse response;
            if(content == null) {
                response = new HttpResponse().setStatusCode(NOT_FOUND_STATUS_CODE).setStatusText(NOT_FOUND_STATUS_TEXT);
            } else {
                response = new HttpResponse().setStatusCode(SUCCESS_STATUS_CODE).setStatusText(SUCCESS_STATUS_TEXT)
                        .setContentType(CONTENT_TYPE_APPLICATION_STREAM)
                        .setContentLength(content.length())
                        .setBody(content);
            }
            return response;
        };
    }

    public static Handler writeFile(String directory) {
        return (request, params) -> {
            String fileNae = params.get("fileName");
            String body = request.getBody();
            HttpResponse response;
            if(RequestUtil.writeFile(directory, fileNae, body)) {
                response = new HttpResponse().setStatusCode(CREATED_STATUS_CODE).setStatusText(CREATED_STATUS_TEXT);
            } else {
                response = new HttpResponse().setStatusCode(ERROR_STATUS_CODE).setStatusText(ERROR_STATUS_TEXT);
            }
            return response;
        };
    }
}
