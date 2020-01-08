package io.vertx.blog.first.momo;

import io.reactivex.Single;
import io.vertx.blog.first.momo.api.HttpServerVerticle;
import io.vertx.blog.first.momo.database.BookDatabaseVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Future;
import io.vertx.reactivex.core.AbstractVerticle;

public class MainVerticle extends AbstractVerticle {
    private static final String HTTP_SERVER_VERTICLE_IDENTIFIER = HttpServerVerticle.class.getName();
    private static final String PG_DATABASE_VERTICLE_IDENTIFIER = BookDatabaseVerticle.class.getName();


    @Override
    public void start(Future<Void> startFuture) throws Exception {
        Single<String> dbVerticleDeployment = vertx.rxDeployVerticle(PG_DATABASE_VERTICLE_IDENTIFIER,
                new DeploymentOptions().setConfig(config().getJsonObject("postgresql.config")));

        dbVerticleDeployment
                .doOnError(startFuture::fail)
                .flatMap(deploymentId -> vertx.rxDeployVerticle(HTTP_SERVER_VERTICLE_IDENTIFIER,
                        new DeploymentOptions().setConfig(config().getJsonObject("http.server.config"))))
                .subscribe(deploymentId -> startFuture.complete(),
                        startFuture::fail);
    }
}

