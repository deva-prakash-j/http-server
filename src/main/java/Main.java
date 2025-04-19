import bean.HttpRequest;
import util.RequestUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Main {

    private static final Set<String> allowedPath = new HashSet<>();

    public static void setAllowedPath() {
        allowedPath.add("/");
        allowedPath.add("");
    }

    public static void handleRequest(Socket socket) {
        try {
            InputStream inputStream = socket.getInputStream();

            HttpRequest request = RequestUtil.getHttpRequest(inputStream);
            String response = "404 Not Found";

            if(allowedPath.contains(request.path())) {
                response = "200 OK";
            }
            socket.getOutputStream().write(String.format("HTTP/1.1 %s\r\n\r\n", response).getBytes());
            socket.getOutputStream().close();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        System.out.println("Started HTTP-SERVER");
        Socket socket;
        setAllowedPath();
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