package util.converter;

import org.apache.commons.lang3.StringUtils;

import models.Document;
import models.Headline;
import models.Paragraph;
import models.Textelement;
import nl.siegmann.epublib.domain.Author;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.domain.Metadata;
import nl.siegmann.epublib.domain.Resource;
import nl.siegmann.epublib.service.MediatypeService;

public class DocumentConverter {
    
    public static Book toEpub(Document doc){

        Book book = new Book();
        Metadata metadata = book.getMetadata();
        metadata.addTitle(doc.title);
        metadata.addAuthor(new Author(doc.givenname, doc.surname));
        
        StringBuilder sb = new StringBuilder();

        for(Textelement element : doc.textelements){
            if(element instanceof Headline){
                Headline headline = (Headline) element;
                int size = headline.size != null ? headline.size : 1;
                sb.append("<h"+size+">");
            }else{
                sb.append("<p>");
            }
            sb.append("");
            sb.append(element.text);
            sb.append("");
            if(element instanceof Headline){
                Headline headline = (Headline) element;
                int size = headline.size != null ? headline.size : 1;
                sb.append("</h"+size+">");
            }else{
                Paragraph para = (Paragraph) element;
                sb.append("</p>");
                if(StringUtils.isNotBlank(para.comment)){
                    sb.append("<p style=\"font-size: 0.8em; font-style: italic;\">");
                    sb.append(para.comment);
                    sb.append("</p>");
                }
            }
        }
        book.addSection("", new Resource(sb.toString().getBytes(), MediatypeService.XHTML));
        
        return book;
    }
    
}
