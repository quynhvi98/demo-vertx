package io.vertx.blog.first.momo.api.handler;

import io.vertx.blog.first.momo.database.BookDatabaseService;
import io.vertx.blog.first.momo.entity.Book;
import io.vertx.core.Handler;
import io.vertx.reactivex.ext.web.RoutingContext;

import static io.vertx.blog.first.momo.api.utils.RestApiUtil.decodeBodyToObject;

public class BookApis {

    /**
     * This handler is for adding a new book.
     */
    public static Handler<RoutingContext> addBookHandler(BookDatabaseService bookDatabaseService) {
        return routingContext -> {
            Book book = decodeBodyToObject(routingContext, Book.class);

//      bookDatabaseService.rxAddNewBook(book)
//        .subscribe(
//          () -> restResponse(routingContext, 200, book.toString()),
//          throwable -> routingContext.fail(new BadRequestException(throwable)));
        };
    }

    /**
     * This handler is for deleting an existing book by id.
     */
    public static Handler<RoutingContext> deleteBookByIdHandler(BookDatabaseService bookDatabaseService) {
        return routingContext -> {
            int bookId = Integer.valueOf(routingContext.pathParam("id"));

//      bookDatabaseService.rxDeleteBookById(bookId)
//        .subscribe(
//          () -> restResponse(routingContext, 202),
//          throwable -> routingContext.fail(new BadRequestException(throwable)));
        };
    }

    /**
     * This handler is for getting an existing book by id.
     */
    public static Handler<RoutingContext> getBookByIdHandler(BookDatabaseService bookDatabaseService) {
        return routingContext -> {
            int bookId = Integer.valueOf(routingContext.pathParam("id"));

//      bookDatabaseService.rxGetBookById(bookId)
//        .subscribe(
//          dbResponse -> {
//            if (dbResponse.isEmpty()) {
//              routingContext.fail(new ResourceNotFoundException("The book with id " + bookId + " can not be found"));
//            } else {
//              restResponse(routingContext, 200, dbResponse.toString());
//            }
//          },
//          throwable -> routingContext.fail(new BadRequestException(throwable))
//        );
        };
    }

    /**
     * This handler is for getting all books or some books by conditions.
     */
    public static Handler<RoutingContext> getBooksHandler(BookDatabaseService bookDatabaseService) {
        return routingContext -> {
            // Get all the query parameters to an object
            Book book = new Book();
            book.setTitle(routingContext.queryParams().get("title"));
            book.setCategory(routingContext.queryParams().get("category"));
            book.setPublicationDate(routingContext.queryParams().get("publicationDate"));
//
//      bookDatabaseService.rxGetBooks(book)
//        .subscribe(
//          dbResponse -> {
//            switch (dbResponse.size()) {
//              case 0:
//                routingContext.fail(new ResourceNotFoundException("The books have not been found"));
//                break;
//              case 1:
//                restResponse(routingContext, 200, dbResponse.getJsonObject(0).toString());
//                break;
//              default:
//                restResponse(routingContext, 200, dbResponse.toString());
//                break;
//            }
//          },
//          throwable -> routingContext.fail(new BadRequestException(throwable))
//        );
        };
    }

    /**
     * This handler is for upserting a book by id.
     */
    public static Handler<RoutingContext> upsertBookByIdHandler(BookDatabaseService bookDatabaseService) {
        return routingContext -> {
            Book book = decodeBodyToObject(routingContext, Book.class);
            int bookId = Integer.valueOf(routingContext.pathParam("id"));

//      bookDatabaseService.rxUpsertBookById(bookId, book)
//        .subscribe(
//          () -> restResponse(routingContext, 200, new JsonObject()
//            .put("id", bookId)
//            .put("title", book.getTitle())
//            .put("category", book.getCategory())
//            .put("publicationDate", book.getPublicationDate())
//            .toString()),
//          throwable -> routingContext.fail(new BadRequestException(throwable))
//        );
        };
    }
}
