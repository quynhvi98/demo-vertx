package io.vertx.blog.first.momo.api.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(Throwable throwable) {
        super(throwable);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
