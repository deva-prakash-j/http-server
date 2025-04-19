import bean.HttpRequest;
import bean.HttpResponse;
import bean.RouteMatch;
import util.RequestUtil;
import util.Router;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {

    private static Router router;

    public static void initiateRouter() {
        router = new Router();
        router.addRoute("", null);
        router.addRoute("/echo/{str}", params -> params.get("str"));
    }

    public static void handleRequest(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();

            HttpRequest request = RequestUtil.getHttpRequest(inputStream);
            HttpResponse response = new HttpResponse();
            RouteMatch match = router.match(request.path());

            if(match != null) {
                response.setStatusCode(200);
                response.setStatusText("OK");
                if(match.handler != null) {
                    String bodyMsg = match.handler.handle(match.pathParam);
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
        Socket socket;
        try(ServerSocket serverSocket = new ServerSocket(4221)){
            serverSocket.setReuseAddress(true);
            System.out.println("Application Started Listening on port 4221");
            while(true){
                socket = serverSocket.accept(); // Wait for connection from client.
                System.out.println("accepted new connection");
                handleRequest(socket);
            }
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }
}