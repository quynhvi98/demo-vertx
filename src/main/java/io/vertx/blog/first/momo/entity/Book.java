package io.vertx.blog.first.momo.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.vertx.codegen.annotations.DataObject;
import io.vertx.core.json.JsonObject;

/**
 * @author Billy Yuan <billy112487983@gmail.com>
 */

@DataObject(generateConverter = true)
@JsonPropertyOrder({"id", "title", "category", "publicationDate"})
public class Book {
    @JsonProperty("id")
    private int id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("category")
    private String category;
    @JsonProperty("publicationDate")
    private String publicationDate;

    public Book() {
        //default constructor for jackson
    }

    public Book(Book other) {
        this.id = other.id;
        this.title = other.title;
        this.category = other.category;
        this.publicationDate = other.publicationDate;
    }

    public Book(String json) {
        this(new JsonObject(json));
    }

    public Book(JsonObject jsonObject) {
//        BookConverter.fromJson(jsonObject, this);
    }

    public Book(int id, String title, String category, String publicationDate) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.publicationDate = publicationDate;
    }

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
//        BookConverter.toJson(this, jsonObject);
        return jsonObject;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Book book = (Book) obj;

        if (id != book.id) return false;
        if (title != null ? !title.equals(book.title) : book.title != null) return false;
        if (category != null ? !category.equals(book.category) : book.category != null) return false;
        return publicationDate != null ? publicationDate.equals(book.publicationDate) : book.publicationDate == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + id;
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (category != null ? category.hashCode() : 0);
        result = 31 * result + (publicationDate != null ? publicationDate.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{\n" +
                "    \"id\": " + this.id + ",\n" +
                "    \"title\": \"" + this.title + "\",\n" +
                "    \"category\": \"" + this.category + "\",\n" +
                "    \"publicationDate\": \"" + this.publicationDate + "\"\n" +
                "}";
    }
}
