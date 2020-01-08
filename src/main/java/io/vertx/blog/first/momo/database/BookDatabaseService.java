package io.vertx.blog.first.momo.database;

import io.reactiverse.pgclient.PgPool;
import io.vertx.blog.first.momo.database.impl.BookDatabaseServiceImpl;
import io.vertx.blog.first.momo.entity.Book;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.codegen.annotations.GenIgnore;
import io.vertx.codegen.annotations.ProxyGen;
import io.vertx.codegen.annotations.VertxGen;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

@VertxGen
@ProxyGen
public interface BookDatabaseService {

    @GenIgnore
    static BookDatabaseService create(PgPool pgPool, Handler<AsyncResult<BookDatabaseService>> resultHandler) {
        return new BookDatabaseServiceImpl(pgPool, resultHandler);
    }

    @GenIgnore
    static BookDatabaseService createProxy(Vertx vertx, String address) {
//    return new BookDatabaseService(new BookDatabaseServiceVertxEBProxy(vertx, address));
        return null;
    }

    @Fluent
    BookDatabaseService addNewBook(Book book, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    BookDatabaseService deleteBookById(int id, Handler<AsyncResult<Void>> resultHandler);

    @Fluent
    BookDatabaseService getBookById(int id, Handler<AsyncResult<JsonObject>> resultHandler);

    @Fluent
    BookDatabaseService getBooks(Book book, Handler<AsyncResult<JsonArray>> resultHandler);

    @Fluent
    BookDatabaseService upsertBookById(int id, Book book, Handler<AsyncResult<Void>> resultHandler);
}
