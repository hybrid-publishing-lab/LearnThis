package models;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
public class Paragraph extends Textelement{

    /*
     * Angular type
     */
    public final static String TYPE = "Paragraph";
    
    public String comment;
    
}
