package constants;

public class ServerConstants {

    public static final int PORT = 4221;
    public static final int THREAD_POOL_SIZE = 10;
    public static final String SUPPORTED_ENCODING = "gzip";

    //Http Code
    public static final int SUCCESS_STATUS_CODE = 200;
    public static final String SUCCESS_STATUS_TEXT = "OK";
    public static final int NOT_FOUND_STATUS_CODE = 404;
    public static final String NOT_FOUND_STATUS_TEXT = "Not Found";
    public static final int NOT_ALLOWED_STATUS_CODE = 405;
    public static final String NOT_ALLOWED_STATUS_TEXT = "Method Not Allowed";
    public static final int CREATED_STATUS_CODE = 201;
    public static final String CREATED_STATUS_TEXT = "Created";
    public static final int ERROR_STATUS_CODE = 500;
    public static final String ERROR_STATUS_TEXT = "INTERNAL SERVER ERROR";

    //content type
    public static final String CONTENT_TYPE_TEXT_PLAIN = "text/plain";
    public static final String CONTENT_TYPE_APPLICATION_STREAM = "application/octet-stream";

    //HTTP Method
    public static final String GET_METHOD = "GET";
    public static final String POST_METHOD = "POST";

    //Header Constants
    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String CONTENT_ENCODING = "Content-Encoding";
    public static final String ACCEPT_ENCODING = "Accept-Encoding";
    public static final String CONNECTION = "Connection";
    public static final String CLOSE = "close";
    public static final String KEEP_ALIVE = "keep-alive";

}
