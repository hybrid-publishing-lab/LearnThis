package controllers;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import models.Card;
import models.Document;
import models.DocumentRepository;
import models.Headline;
import models.Paragraph;
import play.mvc.Controller;
import play.mvc.Result;
import util.JpaFixer;

/**
 * The main set of web services.
 */
@Named
@Singleton
public class Application extends Controller {

    private final DocumentRepository documentRepository;

    // We are using constructor injection to receive a repository to support our desire for immutability.
    @Inject
    public Application(final DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }
    
    public Result index() {
        return ok(views.html.index.render(""));
    }
    
    public Result newDoc(){
        final Document doc = new Document();
        doc.title = "Document";
        doc.givenname = "Vorname";
        doc.surname = "Nachname";
        doc.createdAt = new Date();
        doc.changedAt = new Date();
        doc.password = "";
        documentRepository.save(doc);
        
//        // add headline
//        Headline headline = new Headline();
//        headline.text = "Headline";
//        headline.size = 2;
//        Card card = new Card();
//        card.sort = 0;
//        card.front = headline;
//        card.document = doc;
//        doc.cards.add(card);
        
        // add paragraph
        Paragraph paragraph = new Paragraph();
        paragraph.text = "Paragraph";
        Card card = new Card();
        card.sort = 1;
        card.front = paragraph;
        card.document = doc;
        doc.cards.add(card);
        
        documentRepository.save(doc);
        
        return redirect(routes.Application.findById(doc.id));
    }
    
    public Result findById(Long id){
        Document doc = documentRepository.findOne(id);
        JpaFixer.removeDuplicatesWorkaround(doc);
        return ok(views.html.document.render(doc));
    }
    
    public Result keywords(Long docId){
        Document doc = documentRepository.findOne(docId);
        JpaFixer.removeDuplicatesWorkaround(doc);
        return ok(views.html.keywords.render(doc));
    }
    
    public Result edit(Long id){
        Document doc = documentRepository.findOne(id);
        JpaFixer.removeDuplicatesWorkaround(doc);
        return ok(views.html.documentEdit.render(doc));
    }
    
    public Result learn(Long id){
        Document doc = documentRepository.findOne(id);
        JpaFixer.removeDuplicatesWorkaround(doc);
        return ok(views.html.documentLearn.render(doc));
    }
}
