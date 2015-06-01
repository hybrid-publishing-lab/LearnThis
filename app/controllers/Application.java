package controllers;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import models.Document;
import models.DocumentRepository;
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
    
    public Result help() {
        return ok(views.html.help.render(""));
    }

    public Result imprint() {
        return ok(views.html.imprint.render(""));
    }
    
    public Result newDoc(){
        Document doc = new Document();
        doc.id = -1L;
        return ok(views.html.documentEdit.render(doc));
    }
    
    public Result findById(Long id){
        Document doc = documentRepository.findOne(id);
        documentRepository.incrementVisits(id);
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
        documentRepository.incrementLearnCount(id);
        JpaFixer.removeDuplicatesWorkaround(doc);
        return ok(views.html.documentLearn.render(doc));
    }
}
