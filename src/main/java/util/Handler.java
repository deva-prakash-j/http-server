package util;

import bean.HttpRequest;
import bean.HttpResponse;

import java.util.Map;

@FunctionalInterface
public interface Handler {
    HttpResponse handle(HttpRequest request, Map<String, String> pathParams);
}
