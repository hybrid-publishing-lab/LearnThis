package models;

import java.util.List;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;

@MappedSuperclass
public class Textelement<T> {
    @Id
    @GeneratedValue
    public Long id;

    public String text;

    public Integer sort;
    
    @ManyToOne
    @JoinColumn(name = "document_id")
    public Document document;
    
//    @OneToMany(mappedBy="textelement")
//    public List<Keyword> keywords;
}
