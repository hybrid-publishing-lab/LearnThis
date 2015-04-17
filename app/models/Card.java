package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import util.KeywordParser;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Card {

    @Id
    @GeneratedValue
    public Long id;
    
    @ManyToOne
    @JoinColumn(name = "document_id")
    @JsonIgnore
    public Document document;

    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    public Textelement front;
    
    @OneToOne(cascade=CascadeType.ALL, fetch=FetchType.EAGER)
    public Textelement back;

    @ElementCollection
    @LazyCollection(LazyCollectionOption.FALSE)
    public List<String> keywords = new ArrayList<String>();
    
    public Integer sort;

    // @PreUpdate
    // @PrePersist
    // funktioniert leider nicht, da nur echte Objekt-Properties verändert werden dürfen
    public void updateKeywords() {
        this.keywords.clear();
        List<String> newKeywords = new ArrayList<String>();
        if (front != null) {
            newKeywords = KeywordParser.parseHashTags(front.text);
        }
        if (back != null) {
            newKeywords.addAll(KeywordParser.parseHashTags(back.text));
        }
        
        for (String keywordStr : newKeywords) {
            this.keywords.add(keywordStr);
        }
    }

}
