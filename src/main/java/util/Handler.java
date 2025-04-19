package util;

import bean.HttpRequest;

import java.util.Map;

@FunctionalInterface
public interface Handler {
    String handle(HttpRequest request, Map<String, String> pathParams);
}
