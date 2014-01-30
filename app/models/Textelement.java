package models;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import util.KeywordParser;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
// @MappedSuperclass
    public abstract class Textelement{
    @Id
    @GeneratedValue
    public Long id;

    @Column(length = 5000)
    public String text;

    public Integer sort;
    
    @Enumerated(EnumType.STRING)
    public TextelementType textelementType;

    @ManyToOne
    @JoinColumn(name = "document_id")
    @JsonIgnore
    public Document document;

//    @OneToMany(fetch=FetchType.EAGER, mappedBy = "textelement", cascade = CascadeType.ALL, orphanRemoval=true)
    @ElementCollection(fetch = FetchType.EAGER)
    @JsonIgnore  // TODO Workaround, da die Keywords von Play nicht korrekt geparst werden können
    public Set<String> keywords = new HashSet<String>();
    
//    @OneToMany(fetch=FetchType.EAGER, mappedBy = "textelement", cascade = CascadeType.ALL, orphanRemoval=true)
    @ElementCollection(fetch = FetchType.EAGER)
    @JsonIgnore  // TODO Workaround, da die Keywords von Play nicht korrekt geparst werden können
    public Set<String> metatags = new HashSet<String>();
    
    public Textelement(){
        textelementType = TextelementType.standard;
    }
    
    public abstract String getType();

    public void merge(Textelement ele) {
        this.text = ele.text;
        this.sort = ele.sort;
        this.textelementType = ele.textelementType;
    }

// TODO funktioniert leider nicht und fuehrt zu fehlern
// muss jetzt von hand aufgerufen werden
//    @PreUpdate
//    @PrePersist
    public void updateKeywords() {
        this.keywords.clear();
        List<String> newKeywords = KeywordParser.parseHashTags(text);
        for (String keywordStr : newKeywords) {
            this.keywords.add(keywordStr);
        }
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Textelement other = (Textelement) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
