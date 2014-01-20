package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
//@MappedSuperclass
public abstract class Textelement {
    @Id
    @GeneratedValue
    public Long id;
    
    @Column(length=5000)
    public String text;

    public Integer sort;
    
    public abstract String getType();

    public void merge(Textelement ele) {
        this.text = ele.text;
        this.sort = ele.sort;
    }
    
    @ManyToOne
    @JoinColumn(name = "document_id")
    @JsonIgnore
    public Document document;
    
//    @OneToMany(mappedBy="textelement", cascade=CascadeType.ALL)
//    public List<Keyword> keywords;
}
