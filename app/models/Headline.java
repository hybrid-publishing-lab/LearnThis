package models;

import javax.persistence.Entity;

@Entity
public class Headline extends Textelement{

    /*
     * Angular type
     */
    public final static String TYPE = "Headline";
    
    public Integer size;
    
}
