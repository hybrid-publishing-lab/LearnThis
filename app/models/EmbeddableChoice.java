package models;

import javax.persistence.Embeddable;

@Embeddable
public class EmbeddableChoice {
    public Boolean correct;
    public String text;
}
