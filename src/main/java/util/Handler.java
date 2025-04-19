package util;

import java.util.Map;

@FunctionalInterface
public interface Handler {
    String handle(Map<String, String> pathParams);
}
