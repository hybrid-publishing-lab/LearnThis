package controllers;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import models.Document;
import models.DocumentRepository;
import models.Headline;
import models.Paragraph;
import models.Textelement;
import models.TextelementRepository;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * The main set of web services.
 */
@Named
@Singleton
public class TextelementController extends Controller {

    private final TextelementRepository textelementRepository;
    private final DocumentRepository documentRepository;

    // We are using constructor injection to receive a repository to support our desire for immutability.
    @Inject
    public TextelementController(TextelementRepository textelementRepository, DocumentRepository documentRepository) {
        this.textelementRepository = textelementRepository;
        this.documentRepository = documentRepository;
    }

    public Result newParagraph(Long id) {
        Document doc = documentRepository.findOne(id);
        Paragraph para = new Paragraph();
        para.text = "Neuer Paragraph";
        doc.appendTextElement(para);
        documentRepository.save(doc);
        return ok(Json.toJson(para));
    }

    public Result newHeadline(Long id) {
        Document doc = documentRepository.findOne(id);
        Headline headline = new Headline();
        headline.text = "Neue Headline";
        headline.size = 3;
        doc.appendTextElement(headline);
        documentRepository.save(doc);
        return ok(Json.toJson(headline));
    }

    public Result delete(Long id) {
        Textelement toDelete = textelementRepository.findOne(id);
        textelementRepository.delete(id);
        Document doc = toDelete.document;
        doc.textelements.remove(toDelete);
        documentRepository.save(doc);
        return ok();
    }
}
