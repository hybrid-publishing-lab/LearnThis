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
    public List<Textelement> textelements = new ArrayList<Textelement>();
    
    public Document(){
        this.createdAt = new Date();
        this.changedAt = new Date();
    }
    
    // ensures correct cross referencing
    public void appendTextElement(Textelement element){
        int maxSort = 0;
        for(Textelement ele : this.textelements){
            if(ele.sort != null && maxSort < ele.sort){
                maxSort = ele.sort;
            }
        }
        element.sort = maxSort + 1;
        this.textelements.add(element);
        element.document = this;
    }
    
    public Textelement findTextelement(Long id){
        for(Textelement ele : textelements){
            if(ele.id != null && ele.id.equals(id)){
                return ele;
            }
        }
        return null;
    }
    
    public boolean removeTextelement(Long id){
        Textelement toDelete = findTextelement(id);
        if(toDelete != null){
            this.textelements.remove(toDelete);
            return true;
        }
        return false;
    }
    
    @PreUpdate
    @PrePersist
    public void updateLastModified() {
        changedAt = new Date();
    }
}
