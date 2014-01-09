package models;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Document {
    @Id
    @GeneratedValue
    public Long id;

    public String autor;

    public Date createdAt;
    
    public Date changedAt;
    
    @OneToMany(mappedBy="document")
    public List<Textelement> textelements;
    
    public Document(){
        this.createdAt = new Date();
        this.textelements.add(new Headline());
        this.textelements.add(new Paragraph());
    }
}
