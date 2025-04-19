package util;

import bean.RouteMatch;
import bean.TrieNode;

import java.util.HashMap;
import java.util.Map;

public class Router {

    private final TrieNode root;

    public Router() {
        this.root = new TrieNode();
    }

    public void addRoute(String pattern, Handler handler) {
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

        current.handler = handler;
    }

    public RouteMatch match(String pattern) {
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

        return new RouteMatch(current.handler, params);
    }
}
