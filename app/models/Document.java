package models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
    
//    @OneToMany(fetch=FetchType.EAGER, mappedBy="document")
//    @JoinColumn(name="document_id", referencedColumnName="id")
    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="document")
//    @OneToMany(fetch=FetchType.EAGER, mappedBy="document")
//    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL)
    public List<Textelement> textelements = new ArrayList<Textelement>();
    
    public Document(){
        this.createdAt = new Date();
        this.changedAt = new Date();
    }
}
