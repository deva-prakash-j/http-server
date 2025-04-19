package bean;

import util.Handler;

import java.util.Map;

public class RouteMatch {

    public Handler handler;
    public Map<String, String> pathParam;

    public RouteMatch(Handler handler, Map<String, String> pathParam) {
        this.handler = handler;
        this.pathParam = pathParam;
    }

}
