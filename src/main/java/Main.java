import bean.HttpRequest;
import bean.HttpResponse;
import bean.RouteMatch;
import util.RequestUtil;
import util.Router;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static Router router;
    private static final int THREAD_POOL_SIZE = 10;

    public static void initiateRouter() {
        router = new Router();
        router.addRoute("", null);
        router.addRoute("/echo/{str}", (request, params) -> params.get("str"));
        router.addRoute("/user-agent", (request, params) -> request.getHeaders().getOrDefault("User-Agent", "Unknown"));
    }

    public static void handleRequest(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();

            HttpRequest request = RequestUtil.getHttpRequest(inputStream);
            HttpResponse response = new HttpResponse();
            RouteMatch match = router.match(request.path);

            if(match != null) {
                response.setStatusCode(200);
                response.setStatusText("OK");
                if(match.handler != null) {
                    String bodyMsg = match.handler.handle(request, match.pathParam);
                    if(bodyMsg != null) {
                        response.setContentType("text/plain");
                        response.setContentLength(bodyMsg.length());
                        response.setBody(bodyMsg);
                    }
                }
            } else {
                response.setStatusCode(404);
                response.setStatusText("Not Found");
            }
            socket.getOutputStream().write(response.getBytes());
            socket.getOutputStream().close();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        initiateRouter();
        System.out.println("Started HTTP-SERVER");
        try(ServerSocket serverSocket = new ServerSocket(4221); ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE)){
            serverSocket.setReuseAddress(true);
            System.out.println("Application Started Listening on port 4221");
            while(true){
                Socket socket = serverSocket.accept(); // Wait for connection from client.
                System.out.println("accepted new connection");
                executor.submit(() -> handleRequest(socket));
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}