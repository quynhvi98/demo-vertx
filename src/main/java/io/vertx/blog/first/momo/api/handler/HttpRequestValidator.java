package io.vertx.blog.first.momo.api.handler;

import io.vertx.ext.web.api.validation.ParameterType;
import io.vertx.reactivex.ext.web.api.validation.HTTPRequestValidationHandler;

public class HttpRequestValidator {

    public static HTTPRequestValidationHandler addBookValidationHandler() {
        return HTTPRequestValidationHandler.create()
                .addQueryParam("title", ParameterType.GENERIC_STRING, false)
                .addQueryParam("category", ParameterType.GENERIC_STRING, false)
                .addQueryParam("publicationDate", ParameterType.DATE, false);
    }

    public static HTTPRequestValidationHandler deleteBookByIdValidationHandler() {
        return HTTPRequestValidationHandler.create()
                .addPathParam("id", ParameterType.INT);
    }

    public static HTTPRequestValidationHandler getBookByIdValidationHandler() {
        return HTTPRequestValidationHandler.create()
                .addPathParam("id", ParameterType.INT);
    }

    public static HTTPRequestValidationHandler upsertBookByIdValidationHandler() {
        return HTTPRequestValidationHandler.create()
                .addPathParam("id", ParameterType.INT);
    }

}

