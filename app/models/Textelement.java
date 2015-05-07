package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import util.KeywordParser;

@Entity
// @MappedSuperclass
public abstract class Textelement {
    @Id
    @GeneratedValue
    public Long id;

    @Column(length = 5000)
    public String text;

    @Enumerated(EnumType.STRING)
    public TextelementType textelementType;

    @Column(length = 1000)
    public String metatags = "";

    public Textelement() {
        textelementType = TextelementType.standard;
    }

    public abstract String getType();

    public void merge(Textelement ele) {
        this.text = ele.text;
        this.textelementType = ele.textelementType;
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
