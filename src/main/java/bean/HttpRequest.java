package bean;

public record HttpRequest(
        String method,
        String path,
        String httpVersion
) { }
