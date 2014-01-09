package models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Keyword {
    @Id
    @GeneratedValue
    public Long id;

    public String text;
    
//    @ManyToOne
//    @JoinColumn(name = "textelement_id")
//    public Textelement textelement;
}
