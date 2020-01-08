package io.vertx.blog.first;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.pgclient.PgConnectOptions;
import io.vertx.pgclient.PgPool;
import io.vertx.sqlclient.PoolOptions;
import io.vertx.sqlclient.Row;
import io.vertx.sqlclient.RowSet;
import io.vertx.sqlclient.Tuple;

import java.util.LinkedHashMap;
import java.util.Map;

public class MyFirstVerticle extends AbstractVerticle {

    private Map<Integer, Dog> dogs = new LinkedHashMap<>();

    @Override
    public void start(Future<Void> future) {
        PgConnectOptions connectOptions = new PgConnectOptions()
                .setPort(54320)
                .setHost("localhost")
                .setDatabase("postgres")
                .setUser("postgres")
                .setPassword("root");

        // Pool options
        PoolOptions poolOptions = new PoolOptions()
                .setMaxSize(5);

        // Create the client pool
        PgPool client = PgPool.pool(vertx, connectOptions, poolOptions);

        client.preparedQuery("INSERT INTO book (id, title, category) VALUES ($1, $2, $3)", Tuple.of(11, "Viet", "aww"), ar -> {
            if (ar.succeeded()) {
                RowSet<Row> rows = ar.result();
                System.out.println(rows.rowCount());
            } else {
                System.out.println("Failure: " + ar.cause().getMessage());
            }
        });

        // A simple query
        client.query("SELECT * FROM book WHERE id='1'", ar -> {
            if (ar.succeeded()) {
                RowSet<Row> result = ar.result();
                System.out.println("Got " + result.size() + " rows ");
                System.out.println(result);

            } else {
                System.out.println("Failure: " + ar.cause().getMessage());
            }
//            // Now close the pool
//            client.close();
        });


        createSomeData();

        // Create a router object.
        Router router = Router.router(vertx);

        // Bind "/" to our hello message.
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .putHeader("content-type", "text/html")
                    .end("<h1>Hello from my first Vert.x 3 application</h1>");
        });

        router.route("/assets/*").handler(StaticHandler.create("assets"));

        router.route().handler(BodyHandler.create());
        router.get("/api/whiskies").handler(this::getAll);
        router.get("/api/whiskies/:id").handler(this::getOne);
        router.post("/api/whiskies").handler(this::addOne);
        router.put("/api/whiskies/:id").handler(this::updateOne);
        router.delete("/api/whiskies/:id").handler(this::deleteOne);


        // Create the HTTP server and pass the "accept" method to the request handler.
        vertx
                .createHttpServer()
                .requestHandler(r -> {
                    r.response().end("<h1>Hello from my first " +
                            "Vert.x 3 application</h1>");
                })
                .requestHandler(router::accept)
                .listen(
                        // Retrieve the port from the configuration,
                        // default to 8080.
                        config().getInteger("http.port", 8080),
                        result -> {
                            if (result.succeeded()) {
                                future.complete();
                            } else {
                                future.fail(result.cause());
                            }
                        }
                );

        createSomeDate(
                (nothing) -> startWebApp(
                        (http) -> completeStartup(http, future)
                ), future);
    }

    private void startWebApp(Handler<AsyncResult<HttpServer>> next) {
        //        Create a route object
        Router router = Router.router(vertx);

        router.get("/api/dogs").handler(this::getAll);
        router.route("/api/dogs").handler(BodyHandler.create());
        router.post("api/dogs").handler(this::addOne);
        router.delete("/api/dogs/:id").handler(this::deleteOne);
        router.route("/").handler(routingContext -> {
            HttpServerResponse response = routingContext.response();
            response
                    .putHeader("content-type", "text/html")
                    .end("<h1> Hello from my second Vert.x apps</h1>");
        });
        //Create the http server and pass the accept method to the request handle

        router.route("/assets/*").handler(StaticHandler.create("assets"));

        vertx
                .createHttpServer()
//                .requestHandler(r -> {
//                    r.response().end("<h1> Hello from my first Vert.x apps</h1>");
//                })
                .requestHandler(router::accept)
//                .listen(8080, result -> {
                .listen(
                        config().getInteger("http.port", 8080),
                        next::handle
                );


    }

    private void completeStartup(AsyncResult<HttpServer> http, Future<Void> future) {
        if (http.succeeded()) {
            future.complete();
        } else {
            future.fail(http.cause());
        }
    }

    private void deleteOne(RoutingContext routingContext) {
        String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            Integer idAsInteger = Integer.valueOf(id);
            dogs.remove(idAsInteger);
        }
        routingContext.response().setStatusCode(204).end();
    }

    private void addOne(RoutingContext routingContext) {
        final Dog dog1 = Json.decodeValue(routingContext.getBodyAsString(),
                Dog.class);
        dogs.put(dog1.getId(), dog1);
        routingContext.response()
                .setStatusCode(201)
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(dog1));
    }

    private void getAll(RoutingContext routingContext) {
        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily(dogs.values()));
    }

    private void createSomeDate(Handler<AsyncResult<Void>> next, Future<Void> fut) {
        Dog dog1 = new Dog("dog1", "vn");
        Dog dog2 = new Dog("dog2", "vn");
        dogs.put(dog1.getId(), dog1);
        dogs.put(dog2.getId(), dog2);
    }

    private void createSomeData() {
        Dog bowmore = new Dog("Bowmore 15 Years Laimrig", "Scotland, Islay");
        dogs.put(bowmore.getId(), bowmore);
        Dog talisker = new Dog("Talisker 57° North", "Scotland, Island");
        dogs.put(talisker.getId(), talisker);
    }

    private void getOne(RoutingContext routingContext) {
        final String id = routingContext.request().getParam("id");
        if (id == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            final Integer idAsInteger = Integer.valueOf(id);
            Dog whisky = dogs.get(idAsInteger);
            if (whisky == null) {
                routingContext.response().setStatusCode(404).end();
            } else {
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(whisky));
            }
        }
    }

    private void updateOne(RoutingContext routingContext) {
        final String id = routingContext.request().getParam("id");
        JsonObject json = routingContext.getBodyAsJson();
        if (id == null || json == null) {
            routingContext.response().setStatusCode(400).end();
        } else {
            final Integer idAsInteger = Integer.valueOf(id);
            Dog whisky = dogs.get(idAsInteger);
            if (whisky == null) {
                routingContext.response().setStatusCode(404).end();
            } else {
                whisky.setName(json.getString("name"));
                whisky.setOrigin(json.getString("origin"));
                routingContext.response()
                        .putHeader("content-type", "application/json; charset=utf-8")
                        .end(Json.encodePrettily(whisky));
            }
        }
    }
}
