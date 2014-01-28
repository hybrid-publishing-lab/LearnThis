package controllers;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import models.Document;
import models.DocumentRepository;
import models.Headline;
import models.Paragraph;
import models.TextelementTypes;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * The main set of web services.
 */
@Named
@Singleton
public class TextelementController extends Controller {

    private final DocumentRepository documentRepository;

    // We are using constructor injection to receive a repository to support our desire for immutability.
    @Inject
    public TextelementController(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Result newParagraph(Long id) {
        Document doc = documentRepository.findOne(id);
        if(doc != null){
            Paragraph para = new Paragraph();
            para.text = "Neuer Paragraph";
            doc.appendTextElement(para);
            documentRepository.save(doc);
            return ok(Json.toJson(para));
        }else{
            return notFound();
        }
    }

    public Result newHeadline(Long id) {
        Document doc = documentRepository.findOne(id);
        if(doc != null){
        Headline headline = new Headline();
        headline.text = "Neue Headline";
        headline.size = 3;
        doc.appendTextElement(headline);
        documentRepository.save(doc);
        return ok(Json.toJson(headline));
        }else{
            return notFound();
        }
    }

    public Result delete(Long docId, Long textelementId) {
        Document doc = documentRepository.findOne(docId);
        boolean removed = doc.removeTextelement(textelementId);
        documentRepository.save(doc);
        if (removed) {
            return ok();
        } else {
            return notFound();
        }
    }
    
    public Result getTypes() {
        // TODO convert TextelementType Enum to json
//        return ok("['standard','special']");
        return ok(Json.toJson(new TextelementTypes()));
    }
}
