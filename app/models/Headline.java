package models;

import javax.persistence.Entity;

@Entity
public class Headline extends Textelement {
    
    public Integer size;

    @Override
    public String getType() {
        return "Headline";
    }

    public void merge(Headline headline) {
        super.merge(headline);
        this.size = headline.size;
    }

    @Override
    public String toString() {
        return "Headline [size=" + size + ", id=" + id + ", sort=" + sort + "]";
    }
    
}
