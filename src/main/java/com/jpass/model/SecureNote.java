package com.jpass.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Représente une note sécurisée.
 */
@JsonTypeName("note")
public class SecureNote extends Entry {
    @JsonProperty("content")
    private String content;
    @JsonProperty("category")
    private String category;

    // Default constructor for Jackson
    public SecureNote() {
        super();
    }

    @JsonCreator
    public SecureNote(
            @JsonProperty("title") String title,
            @JsonProperty("content") String content) {
        super(title);
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
        addModification();
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
        addModification();
    }
}