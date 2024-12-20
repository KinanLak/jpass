package com.jpass.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.LocalDateTime;
import java.util.*;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.CLASS, // Change to use full class name
    include = JsonTypeInfo.As.PROPERTY,
    property = "@class"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PasswordEntry.class),
    @JsonSubTypes.Type(value = SecureNote.class),
    @JsonSubTypes.Type(value = CreditCardEntry.class),
    @JsonSubTypes.Type(value = IdentityEntry.class)
})
public abstract class Entry {
    @JsonProperty("id")
    protected String id;
    
    @JsonProperty("title")
    protected String title;
    
    @JsonProperty("creationDate")
    protected LocalDateTime creationDate;
    
    @JsonProperty("modificationDates")
    protected List<LocalDateTime> modificationDates;
    
    @JsonProperty("tags")
    protected Set<String> tags;
    
    @JsonProperty("notes")
    protected String notes;

    // Default constructor for Jackson
    protected Entry() {
        this.creationDate = LocalDateTime.now();
        this.modificationDates = new ArrayList<>();
        this.tags = new HashSet<>();
    }

    @JsonCreator
    protected Entry(@JsonProperty("title") String title) {
        this();
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    // Change the getters to return mutable collections for serialization
    public List<LocalDateTime> getModificationDates() {
        return new ArrayList<>(modificationDates);
    }

    public Set<String> getTags() {
        return new HashSet<>(tags);
    }

    public void addModification() {
        this.modificationDates.add(LocalDateTime.now());
    }

    public void addTag(String tag) {
        this.tags.add(tag);
    }

    public void removeTag(String tag) {
        this.tags.remove(tag);
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }
}