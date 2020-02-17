package example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.jdbc.JDBCClient;
import io.vertx.ext.sql.SQLConnection;

import static example.db.DBConFig.SQL_CREATE_PAGES_TABLE;

public class MainVerticle extends AbstractVerticle {

    private JDBCClient dbClient;
    private static final Logger LOGGER = LoggerFactory.getLogger(MainVerticle.class);
    @Override
    public void start(Promise<Void> promise) throws Exception {
        Future<Void> steps = prepareDatabase().compose(v -> startHttpServer());
        steps.setHandler(ar -> {
            if (ar.succeeded()) {
                promise.complete();
            } else {
                promise.fail(ar.cause());
            }
        });
    }

    private Future<Void> prepareDatabase() {
        Promise<Void> promise = Promise.promise();

        dbClient = JDBCClient.createShared(vertx, new JsonObject()
                .put("url", "jdbc:hsqldb:file:db/wiki")
                .put("driver_class", "org.hsqldb.jdbcDriver")
                .put("max_pool_size", 30));

        dbClient.getConnection(ar -> {
            if (ar.failed()) {
                LOGGER.error("Could not open a database connection", ar.cause());
                promise.fail(ar.cause());
            } else {
                SQLConnection connection = ar.result();
                connection.execute(SQL_CREATE_PAGES_TABLE, create -> {
                    connection.close();
                    if (create.failed()) {
                        LOGGER.error("Database preparation error", create.cause());
                        promise.fail(create.cause());
                    } else {
                        promise.complete();
                    }
                });
            }
        });

        return promise.future();
    }

    private Future<Void> startHttpServer() {
        Promise<void> promise = Promise.promise();
        // (...)
        return (Future<Void>) promise.future();
    }

}
