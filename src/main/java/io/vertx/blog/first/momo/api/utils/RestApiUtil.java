package io.vertx.blog.first.momo.api.utils;

import io.vertx.core.json.DecodeException;
import io.vertx.core.json.Json;
import io.vertx.reactivex.ext.web.RoutingContext;

public class RestApiUtil {
    public static void restResponse(RoutingContext routingContext, int statusCode, String body) {
        routingContext.response().putHeader("Content-Type", "application/json; charset=utf-8");
        routingContext.response().setStatusCode(statusCode)
                .end(body);
    }

    public static void restResponse(RoutingContext routingContext, int statusCode) {
        restResponse(routingContext, statusCode, "");
    }

    public static <T> T decodeBodyToObject(RoutingContext routingContext, Class<T> clazz) {
        try {
            return Json.decodeValue(routingContext.getBodyAsString("UTF-8"), clazz);
        } catch (DecodeException exception) {
            routingContext.fail(exception);
            return null;
        }
    }
}
