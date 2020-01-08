package model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
public class Article {
    private String id;
    private String content;
    private String author;
    private String datePublished;
    private int wordCount;

    public Article(String id, String content, String author, String datePublished, int wordCount) {
        super();
        this.id = id;
        this.content = content;
        this.author = author;
        this.datePublished = datePublished;
        this.wordCount = wordCount;
    }

}
