package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

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
    
//    @OneToMany(mappedBy="document", fetch=FetchType.EAGER)
    // TODO warum funktioniert hier das cascade nicht?
//    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    @OneToMany(fetch=FetchType.EAGER)
    @JoinColumn(name="document_id", referencedColumnName="id")
    public List<Textelement> textelements = new ArrayList<Textelement>();
    
//    public Document(){
//        this.createdAt = new Date();
//        this.textelements.add(new Headline());
//        this.textelements.add(new Paragraph());
//    }
}
