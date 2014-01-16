package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

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
    
//    @ManyToOne
//    @JoinColumn(name = "document_id")
//    public Document document;
    
//    @OneToMany(mappedBy="textelement")
//    public List<Keyword> keywords;
}
