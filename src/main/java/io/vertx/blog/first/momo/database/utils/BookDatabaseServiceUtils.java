
package io.vertx.blog.first.momo.database.utils;

import io.reactiverse.reactivex.pgclient.Tuple;
import io.vertx.blog.first.momo.entity.Book;
import io.vertx.core.json.JsonObject;

import java.util.Optional;


public class BookDatabaseServiceUtils {

  private static final String SQL_FIND_BOOKS_CONDITION_BY_TITLE = " AND title = $";
  private static final String SQL_FIND_BOOKS_CONDITION_BY_CATEGORY = " AND category = $";
  private static final String SQL_FIND_BOOKS_CONDITION_BY_PUBLICATION_DATE = " AND publication_date = $";

  public static JsonObject emptyJsonObject() {
    return new JsonObject();
  }

  // generate query with dynamic where clause in a manual way
  public static DynamicQuery generateDynamicQuery(String rawSql, Book book) {
    Optional<String> title = Optional.ofNullable(book.getTitle());
    Optional<String> category = Optional.ofNullable(book.getCategory());
    Optional<String> publicationDate = Optional.ofNullable(book.getPublicationDate());

    // Concat the SQL by conditions
    int count = 0;
    String dynamicSql = rawSql;
    Tuple params = Tuple.tuple();
    if (title.isPresent()) {
      count++;
      dynamicSql += SQL_FIND_BOOKS_CONDITION_BY_TITLE;
      dynamicSql += count;
      params.addString(title.get());
    }
    if (category.isPresent()) {
      count++;
      dynamicSql += SQL_FIND_BOOKS_CONDITION_BY_CATEGORY;
      dynamicSql += count;
      params.addString(category.get());
    }
    if (publicationDate.isPresent()) {
      count++;
      dynamicSql += SQL_FIND_BOOKS_CONDITION_BY_PUBLICATION_DATE;
      dynamicSql += count;
      params.addValue(publicationDate.get());
    }
    return new DynamicQuery(dynamicSql, params);
  }

  public static class DynamicQuery {
    private String preparedQuery;
    private Tuple params;

    public DynamicQuery(String preparedQuery, Tuple params) {
      this.preparedQuery = preparedQuery;
      this.params = params;
    }

    public String getPreparedQuery() {
      return preparedQuery;
    }

    public Tuple getParams() {
      return params;
    }
  }
}
