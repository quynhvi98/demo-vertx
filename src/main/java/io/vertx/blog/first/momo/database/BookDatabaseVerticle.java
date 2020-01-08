package io.vertx.blog.first.momo.database;

import io.reactiverse.pgclient.PgClient;
import io.reactiverse.pgclient.PgPool;
import io.reactiverse.pgclient.PgPoolOptions;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.serviceproxy.ServiceBinder;

public class BookDatabaseVerticle extends AbstractVerticle {
    private static final String CONFIG_PG_HOST = "postgresql.host";
    private static final String CONFIG_PG_PORT = "postgresql.port";
    private static final String CONFIG_PG_DATABASE = "postgresql.database";
    private static final String CONFIG_PG_USERNAME = "postgresql.username";
    private static final String CONFIG_PG_PASSWORD = "postgresql.password";
    private static final String CONFIG_PG_POOL_MAX_SIZE = "postgresql.pool.maxsize";

    private static final String CONFIG_DB_EB_QUEUE = "library.db.eb.address";

    private static final Logger LOGGER = LoggerFactory.getLogger(BookDatabaseVerticle.class);

    private PgPool pgPool;

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        PgPoolOptions pgPoolOptions = new PgPoolOptions()
                .setHost(config().getString(CONFIG_PG_HOST, "127.0.0.1"))
                .setPort(config().getInteger(CONFIG_PG_PORT, 5432))
                .setDatabase(config().getString(CONFIG_PG_DATABASE))
                .setUser(config().getString(CONFIG_PG_USERNAME))
                .setPassword(config().getString(CONFIG_PG_PASSWORD))
                .setMaxSize(config().getInteger(CONFIG_PG_POOL_MAX_SIZE, 20));

        this.pgPool = PgClient.pool(vertx, pgPoolOptions);

        String databaseEbAddress = config().getString(CONFIG_DB_EB_QUEUE);

        BookDatabaseService.create(pgPool, result -> {
            if (result.succeeded()) {
                // register the database service
                new ServiceBinder(vertx)
                        .setAddress(databaseEbAddress)
                        .register(BookDatabaseService.class, result.result())
                        .exceptionHandler(throwable -> {
                            LOGGER.error("Failed to establish PostgreSQL database service", throwable);
                            startFuture.fail(throwable);
                        })
                        .completionHandler(res -> {
                            LOGGER.info("PostgreSQL database service is successfully established in \"" + databaseEbAddress + "\"");
                            startFuture.complete();
                        });
            } else {
                LOGGER.error("Failed to initiate the connection to database", result.cause());
                startFuture.fail(result.cause());
            }
        });
    }

    @Override
    public void stop() throws Exception {
        pgPool.close();
    }
}
