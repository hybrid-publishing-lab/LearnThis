package models;

import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

@Entity
public class Document {
    @Id
    @GeneratedValue
    public Long id;

    public String surname;

    public String givenname;

    public String title;
    
    public Date createdAt;
    
    public Date changedAt;
    
    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="document", orphanRemoval=true)
    @OrderBy("sort ASC")
    public Set<Textelement> textelements = new TreeSet<Textelement>();
    
    public Document(){
        this.createdAt = new Date();
        this.changedAt = new Date();
    }
    
    // ensures correct cross referencing
    public void addTextElement(Textelement element){
        this.textelements.add(element);
        element.document = this;
    }
    
    @PreUpdate
    @PrePersist
    public void updateLastModified() {
        changedAt = new Date();
    }
}
