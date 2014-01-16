package util.converter;

import models.Document;
import models.Headline;
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
                sb.append("</p>");
            }
        }
        book.addSection("", new Resource(sb.toString().getBytes(), MediatypeService.XHTML));
        
        return book;
    }
    
}
