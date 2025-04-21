package util;

import bean.HttpResponse;
import bean.RouteMatch;
import bean.TrieNode;

import java.util.HashMap;
import java.util.Map;

import static constants.ServerConstants.*;

public class Router {

    private static Router instance;
    private final TrieNode root;

    private Router() {
        this.root = new TrieNode();
    }

    public static Router getInstance() {
        if (instance == null) {
            instance = new Router();
        }
        return instance;
    }

    public void setupRouter(String directory) {
        instance.addRoute(GET_METHOD, "", (request, param) -> new HttpResponse()
                .setStatusCode(SUCCESS_STATUS_CODE).setStatusText(SUCCESS_STATUS_TEXT));
        instance.addRoute(GET_METHOD, "/echo/{str}", (request, params) -> {
            String str = params.get("str");
            return new HttpResponse().setStatusCode(SUCCESS_STATUS_CODE).setStatusText(SUCCESS_STATUS_TEXT)
                    .setContentType(CONTENT_TYPE_TEXT_PLAIN)
                    .setContentLength(str.length())
                    .setBody(str);});
        instance.addRoute(GET_METHOD, "/user-agent", (request, params) -> {
            String userAgent = request.getHeaders().getOrDefault("User-Agent", "Unknown");
            return new HttpResponse().setStatusCode(SUCCESS_STATUS_CODE).setStatusText(SUCCESS_STATUS_TEXT)
                    .setContentType(CONTENT_TYPE_TEXT_PLAIN)
                    .setContentLength(userAgent.length())
                    .setBody(userAgent);
        });
        instance.addRoute(GET_METHOD, "/files/{fileName}", HandlerImpl.readFile(directory));
        instance.addRoute(POST_METHOD, "/files/{fileName}", HandlerImpl.writeFile(directory));
    }

    public void addRoute(String method, String pattern, Handler handler) {
        String[] parts = pattern.split("/");
        TrieNode current = root;
        for (String part: parts) {
            if (part.isEmpty()) continue;

            if(part.startsWith("{") && part.endsWith("}")) {
                if (current.paramChild == null) {
                    current.paramChild = new TrieNode();
                    current.paramChild.paramName = part.substring(1, part.length() - 1);
                }
                current = current.paramChild;
            } else {
                current.children.putIfAbsent(part, new TrieNode());
                current = current.children.get(part);
            }
        }

        current.handlers.put(method.toUpperCase(), handler);
    }

    public RouteMatch match(String method, String pattern) {
        String[] parts = pattern.split("/");
        TrieNode current = root;
        Map<String, String> params = new HashMap<>();
        for (String part: parts) {
            if (part.isEmpty()) continue;

            if(current.children.containsKey(part)) {
                current = current.children.get(part);
            } else if(current.paramChild != null) {
                params.put(current.paramChild.paramName, part);
                current = current.paramChild;
            } else {
                return null;
            }
        }

        Handler handler = current.handlers.get(method.toUpperCase());
        if(handler == null) {
            handler = ResponseUtil.getMethodNotSupportedHandler();
        }

        return new RouteMatch(handler, params);
    }
}
