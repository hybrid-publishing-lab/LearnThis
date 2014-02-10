package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import util.TextelementSortComparator;

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
    
    public Set<String> getKeywords(){
        Set<String> result = new HashSet<String>();
        for(Textelement ele : this.textelements){
            if(ele instanceof Paragraph){
                Paragraph para = (Paragraph) ele;
                result.addAll(para.keywords);
            }
        }
        return result;
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

    /**
     * Fügt ein Textelement an einem index ein.
     * Sort wird für das Element und alle nachfolgenden richtig gesetzt.
     * Es wird dabei auch der Fall betrachtet, dass sort nicht fortlaufend und von 0 an ausgehend durchnummeriert ist.
     * @param e Textelement
     * @param index an dem das Element eingefuegt werden soll
     */
    public void insertTextElement(Textelement e, Integer index) {
        if(index <= this.textelements.size()){
            int sort = this.textelements.size();
            boolean indexFound = false;
            Collections.sort(this.textelements, new TextelementSortComparator());
            for(int i = 0 ; i < textelements.size() ; i++){
                Textelement ele = textelements.get(i);
                // merke den sort wert des elementes mit dem index
                if(ele.sort != null && i == index){
                    sort = ele.sort;
                    indexFound = true;
                }
                // erhoehe sort fuer alle nachfolgenden element
                if(indexFound){
                    ele.sort = ele.sort +1;
                }
            }
            e.document = this;
            e.sort = sort;
            this.textelements.add(index, e);
        }else{
            appendTextElement(e);
        }
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
        Document other = (Document) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }

    
    
}
