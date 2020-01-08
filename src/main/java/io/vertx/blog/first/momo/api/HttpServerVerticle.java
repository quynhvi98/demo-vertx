package io.vertx.blog.first.momo.api;

import io.vertx.blog.first.momo.api.handler.BookApis;
import io.vertx.blog.first.momo.api.handler.FailureHandler;
import io.vertx.blog.first.momo.database.BookDatabaseService;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import io.vertx.reactivex.ext.web.api.validation.HTTPRequestValidationHandler;
import io.vertx.reactivex.ext.web.handler.BodyHandler;

import static io.vertx.blog.first.momo.api.handler.HttpRequestValidator.*;

public class HttpServerVerticle extends AbstractVerticle {
    private static final String CONFIG_HTTP_SERVER_PORT = "http.server.port";
    private static final String CONFIG_DB_EB_QUEUE = "library.db.eb.address";

    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerVerticle.class);

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        HttpServer httpServer = vertx.createHttpServer();

        BookDatabaseService bookDatabaseService = BookDatabaseService.createProxy(vertx.getDelegate(), config().getString(CONFIG_DB_EB_QUEUE));

        Router router = Router.router(vertx);

        router.route().handler(BodyHandler.create());

        router.route().handler(HTTPRequestValidationHandler.create().addExpectedContentType("application/json"));

        router.get(EndPoints.GET_BOOKS).handler(addBookValidationHandler())
                .handler(BookApis.getBooksHandler(bookDatabaseService));

        router.post(EndPoints.ADD_NEW_BOOK).handler(BookApis.addBookHandler(bookDatabaseService));

        router.delete(EndPoints.DELETE_BOOK_BY_ID).handler(deleteBookByIdValidationHandler())
                .handler(BookApis.deleteBookByIdHandler(bookDatabaseService));

        router.get(EndPoints.GET_BOOK_BY_ID).handler(getBookByIdValidationHandler())
                .handler(BookApis.getBookByIdHandler(bookDatabaseService));

        router.put(EndPoints.UPDATE_BOOK_BY_ID).handler(upsertBookByIdValidationHandler())
                .handler(BookApis.upsertBookByIdHandler(bookDatabaseService));

        router.route().failureHandler(new FailureHandler());

        int httpServerPort = config().getInteger(CONFIG_HTTP_SERVER_PORT, 8080);
        httpServer.requestHandler(router::accept)
                .rxListen(httpServerPort)
                .subscribe(server -> {
                            LOGGER.info("HTTP server is running on port " + httpServerPort);
                            startFuture.complete();
                        },
                        throwable -> {
                            LOGGER.error("Fail to start a HTTP server ", throwable);
                            startFuture.fail(throwable);
                        });
    }
}
