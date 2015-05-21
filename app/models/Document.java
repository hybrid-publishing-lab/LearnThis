package models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import util.CardSortComparator;

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
    
    public String description;
    
    @Column(columnDefinition="bigint(20) default '0'")
    public Long visits = 0L;

    @Column(columnDefinition="bigint(20) default '0'")
    public Long learnCount = 0L;
    
    @JsonIgnore
    public String password;
    
    @OneToMany(fetch=FetchType.EAGER, cascade = CascadeType.ALL, mappedBy="document", orphanRemoval=true)
    @OrderBy("sort ASC")
    public List<Card> cards = new ArrayList<Card>();
    
    public Document(){
        this.createdAt = new Date();
        this.changedAt = new Date();
    }
    
    public Set<String> getKeywords(){
        Set<String> result = new HashSet<String>();
        for(Card card : this.cards){
            result.addAll(card.keywords);
        }
        return result;
    }
    
    // ensures correct cross referencing
    public void appendCard(Card card){
        int maxSort = 0;
        for(Card c : this.cards){
            if(c.sort != null && maxSort < c.sort){
                maxSort = c.sort;
            }
        }
        card.sort = maxSort + 1;
        this.cards.add(card);
        card.document = this;
    }

    /**
     * Fügt ein Textelement an einem index ein.
     * Sort wird für das Element und alle nachfolgenden richtig gesetzt.
     * Es wird dabei auch der Fall betrachtet, dass sort nicht fortlaufend und von 0 an ausgehend durchnummeriert ist.
     * @param card Textelement
     * @param index an dem das Element eingefuegt werden soll
     */
    public void insertCard(Card card, Integer index) {
        if(index <= this.cards.size()){
            int sort = this.cards.size();
            boolean indexFound = false;
            Collections.sort(this.cards, new CardSortComparator());
            for(int i = 0 ; i < cards.size() ; i++){
                Card c = cards.get(i);
                // merke den sort wert des elementes mit dem index
                if(c.sort != null && i == index){
                    sort = c.sort;
                    indexFound = true;
                }
                // erhoehe sort fuer alle nachfolgenden element
                if(indexFound){
                    c.sort = c.sort +1;
                }
            }
            card.document = this;
            card.sort = sort;
            this.cards.add(index, card);
        }else{
            appendCard(card);
        }
    }
    
    public Card findCard(Long id){
        for(Card card : cards){
            if(card.id != null && card.id.equals(id)){
                return card;
            }
        }
        return null;
    }
    
    public boolean removeCard(Long id){
        Card toDelete = findCard(id);
        if(toDelete != null){
            this.cards.remove(toDelete);
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
