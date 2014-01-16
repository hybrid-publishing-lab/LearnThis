package util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import models.Document;
import models.Headline;
import models.Paragraph;

public class Importer {
    public static Document importFromTextfile(File file, String encoding) throws Exception{
        Document doc = new Document();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
            List<String> textelements = parseTextelements(br);
            for(String element : textelements){
                if(element.length() <= 200){
                    // Headline
                    Headline hl = new Headline();
                    hl.text = element;
                    hl.size = 1;
                    doc.textelements.add(hl);
                }else{
                    // Paragraph
                    Paragraph pg = new Paragraph();
                    pg.text = element;
                    doc.textelements.add(pg);
                }
            }
        }finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                }
            }
        }
        return doc;
    }

    private static List<String> parseTextelements(BufferedReader br) throws IOException {

        List<String> textelements = new ArrayList<String>();
        String line = null;
        String textelement = "";
        while ((line = br.readLine()) != null) {
            if (isEmpty(line)) {
                // Separator
                if (!isEmpty(textelement)) {
                    textelements.add(textelement);
                    textelement = "";
                }
            } else {
                // Textelement
                textelement += line;
            }
        }
        return textelements;
    }

    private static Boolean isEmpty(String s) {
        return s.matches("\\s*");
    }

}
