package models;

import javax.persistence.Entity;

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

    @Override
    public String toString() {
        return "Paragraph [comment=" + comment + ", id=" + id + ", sort=" + sort + "]";
    }

}
