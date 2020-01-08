package io.vertx.blog.first.momo.api;

public final class EndPoints {
    public static final String GET_BOOKS = "/books";
    public static final String ADD_NEW_BOOK = "/books";
    public static final String GET_BOOK_BY_ID = "/books/:id";
    public static final String DELETE_BOOK_BY_ID = "/books/:id";
    public static final String UPDATE_BOOK_BY_ID = "/books/:id";

    private EndPoints() {
        // No instance of this class allowed
    }
}
