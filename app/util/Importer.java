package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import models.Card;
import models.Document;
import models.MultipleChoice;

import org.apache.commons.lang3.StringUtils;

import play.Logger;

public class Importer {
    public static Document importFromTextfile(File file, String encoding, boolean splitText) throws Exception{
        Document doc = new Document();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
            fillDocWithText(doc, br, splitText);
        } catch(Exception e) {
            Logger.error("Error importing Text from file " + file.getAbsolutePath(), e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Logger.error("Error while closing BufferedReader", e);
                }
            }
        }
        return doc;
    }

    public static Document importFromText(String freitext, String encoding, boolean splitText) {
        Document doc = new Document();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new StringReader(freitext));
            fillDocWithText(doc, br, splitText);
        } catch(Exception e) {
            Logger.error("Error importing Text: " + freitext, e);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    Logger.error("Error while closing BufferedReader", e);
                }
            }
        }
        return doc;
    }


    private static void fillDocWithText(Document doc, BufferedReader br, boolean splitText) throws IOException {
        List<String> textelements = parseTextelements(br, splitText);
        int i = 0;
        for(String element : textelements){
            MultipleChoice mc = new MultipleChoice();
            mc.text = element;
            Card card = new Card();
            card.sort = i;
            card.front = mc;
            card.back = new MultipleChoice();
            doc.cards.add(card);
            card.document = doc;
            i++;
        }
    }
    
    private static List<String> parseTextelements(BufferedReader br, boolean splitText) throws IOException {

        List<String> textelements = new ArrayList<String>();
        String line = null;
        String textelement = "";
        while ((line = br.readLine()) != null) {
            if (isEmpty(line)) {
                // Separator
                if (!isEmpty(textelement)) {
                    if (splitText) {
                        textelements.add(textelement);
                        textelement = "";
                    } else {
                        textelement += "\n" + line;
                    }
                }
            } else {
                // Textelement
                textelement += line;
            }
        }
        // add last element
        if(StringUtils.isNotEmpty(textelement)){
            textelements.add(textelement);
        }
        return textelements;
    }

    private static Boolean isEmpty(String s) {
        return s.matches("\\s*");
    }
}
