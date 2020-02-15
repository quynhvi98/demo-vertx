package example;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Promise;

public class MainVerticle extends AbstractVerticle {
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
        Promise<void> promise = Promise.promise();
        // (...)
        return (Future<Void>) promise.future();
    }

    private Future<Void> startHttpServer() {
        Promise<void> promise = Promise.promise();
        // (...)
        return (Future<Void>) promise.future();
    }

}
