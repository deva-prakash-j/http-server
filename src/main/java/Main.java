import bean.HttpRequest;
import bean.HttpResponse;
import bean.RouteMatch;
import static constants.ServerConstants.*;
import util.RequestUtil;
import util.Router;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    private static Router router;
    private static String directory;

    public static void handleRequest(Socket socket) {
        try (socket) {
            socket.setSoTimeout(30000);
            InputStream inputStream = socket.getInputStream();
            OutputStream outputStream = socket.getOutputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);

            while(true) {

                HttpRequest request = RequestUtil.getHttpRequest(bufferedInputStream);
                System.out.println("Received request: " + request.method + " " + request.path);
                HttpResponse response;
                RouteMatch match = router.match(request.method, request.path);

                if(match != null) {
                    response = match.handler.handle(request, match.pathParam);
                } else {
                    response = new HttpResponse().setStatusCode(NOT_FOUND_STATUS_CODE).setStatusText(NOT_FOUND_STATUS_TEXT);
                }
                outputStream.write(response.getBytes(request.getHeaders().get(CONNECTION)));
                if(response.isEncoded) {
                    outputStream.write(response.getBodyBytes());
                }

                if (CLOSE.equalsIgnoreCase(request.getHeaders().get(CONNECTION))) {
                    break;
                }
                outputStream.flush();
            }
            //socket.getOutputStream().close();
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        if (args.length > 1 && args[0].equals("--directory")) {
            directory = args[1];
        }
        router = Router.getInstance();
        router.setupRouter(directory);
        System.out.println("Started HTTP-SERVER");
        try(ServerSocket serverSocket = new ServerSocket(PORT); ExecutorService executor = Executors.newFixedThreadPool(THREAD_POOL_SIZE)){
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