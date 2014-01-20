package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

@Entity
public class Paragraph extends Textelement {

    public String comment;

    @Override
    public String getType() {
        return "Paragraph";
    }

    public void merge(Paragraph para) {
        super.merge(para);
        this.comment = para.comment;
    }

}
