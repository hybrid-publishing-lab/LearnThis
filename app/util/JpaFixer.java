package util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import models.Document;
import models.Textelement;
import play.Logger;

public class JpaFixer {
    /**
     * TODO THIS IS JUST A WORKAROUND TO REMOVE DUPLICATE ENTRIES IN THE TEXTELEMENT COLLECTION !!!
     * REMOVE THIS AND FIX THIS JPA ISSUE !!!
     * @param doc
     */
    public static void removeDuplicatesWorkaround(Document doc){
        List<Textelement> textelements = doc.textelements;
        Map<Long, Textelement> uniqueElements = new HashMap<Long, Textelement>();
        for(int i = 0 ; i < textelements.size() ; i++){
            Textelement value = textelements.get(i);
            Long key = value.id;
            if(uniqueElements.containsKey(key)){
                textelements.remove(i);
                i--;
                Logger.debug("Textelement removed from Document: "+value);
            }else{
                uniqueElements.put(key, value);
            }
        }
    }
    
    /**
     * THIS IS JUST A WORKAROUND TO REMOVE DUPLICATE ENTRIES IN THE TEXTELEMENT COLLECTION !!!
     * REMOVE THIS AND FIX THIS JPA ISSUE !!!
     * @param doc
     */
    public static void removeDuplicatesWorkaround(List<Document> docs){
        for(Document doc : docs){
            removeDuplicatesWorkaround(doc);
        }
    }
}
