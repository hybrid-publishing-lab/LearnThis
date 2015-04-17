package models;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

@Entity
public class MultipleChoice extends Textelement {

    public String comment;
    
    @LazyCollection(LazyCollectionOption.FALSE)
    @ElementCollection
//    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(
          name="multiplechoice",
          joinColumns=@JoinColumn(name="Textelement_id")
    )
    public List<EmbeddableChoice> choices;
    
    @Override
    public String getType() {
        return "MultipleChoice";
    }

    public void merge(MultipleChoice ele) {
        super.merge(ele);
        this.comment = ele.comment;
    }

    @Override
    public String toString() {
        return "Paragraph [comment=" + comment + ", id=" + id + "]";
    }

}
