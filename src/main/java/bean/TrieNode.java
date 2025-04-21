package bean;

import util.Handler;

import java.util.HashMap;
import java.util.Map;

public class TrieNode {
    public Map<String, TrieNode> children = new HashMap<>();
    public TrieNode paramChild = null;
    public String paramName = null;
    public Map<String, Handler> handlers = new HashMap<>();
}
