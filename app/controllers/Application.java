package controllers;

import java.util.Date;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import models.Document;
import models.DocumentRepository;
import models.Headline;
import models.HeadlineRepository;
import models.Paragraph;
import models.ParagraphRepository;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * The main set of web services.
 */
@Named
@Singleton
public class Application extends Controller {

    private final DocumentRepository documentRepository;
    private final ParagraphRepository paragraphRepository;
    private final HeadlineRepository headlineRepository;

    // We are using constructor injection to receive a repository to support our desire for immutability.
    @Inject
    public Application(final DocumentRepository documentRepository, ParagraphRepository paragraphRepository,
            HeadlineRepository headlineRepository) {
        this.documentRepository = documentRepository;
        this.paragraphRepository = paragraphRepository;
        this.headlineRepository = headlineRepository;
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
        // add headline
        Headline headline = new Headline();
        headline.text = "Headline";
        headlineRepository.save(headline);
        doc.textelements.add(headline);
        // add paragraph
        Paragraph paragraph = new Paragraph();
        paragraph.text = "Paragraph";
        paragraphRepository.save(paragraph);
        doc.textelements.add(paragraph);
        documentRepository.save(doc);
        
        return redirect(routes.Application.findById(doc.id));// 
//        return ok(views.html.document.render("new"));
    }
    
    public Result findById(Long id){
        Document doc = documentRepository.findOne(id);
        return ok(views.html.document.render(doc));
    }
}
