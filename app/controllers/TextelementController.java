package controllers;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import models.Document;
import models.DocumentRepository;
import models.Headline;
import models.Paragraph;
import models.TextelementType;

import org.apache.commons.lang3.StringUtils;

import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.JpaFixer;

import com.fasterxml.jackson.databind.JsonNode;

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

    public Result newParagraph(Long id, Integer index) {
        Document doc = documentRepository.findOne(id);
        JpaFixer.removeDuplicatesWorkaround(doc);
        JsonNode json = request().body().asJson();
        if (doc != null) {
            Paragraph para = new Paragraph();
            para.text = "Neuer Paragraph";
            if (json != null && StringUtils.isNotEmpty(json.asText())) {
                para.text = json.asText();
            }
            doc.insertTextElement(para, index);
            documentRepository.save(doc);
            return ok(Json.toJson(para));
        } else {
            return notFound();
        }
    }

    public Result newHeadline(Long id, Integer index) {
        Document doc = documentRepository.findOne(id);
        JpaFixer.removeDuplicatesWorkaround(doc);
        if (doc != null) {
            Headline headline = new Headline();
            headline.text = "Neue Headline";
            headline.size = 3;
            doc.insertTextElement(headline, index);
            documentRepository.save(doc);
            return ok(Json.toJson(headline));
        } else {
            return notFound();
        }
    }

    public Result delete(Long docId, Long textelementId) {
        Document doc = documentRepository.findOne(docId);
        JpaFixer.removeDuplicatesWorkaround(doc);
        boolean removed = doc.removeTextelement(textelementId);
        documentRepository.save(doc);
        if (removed) {
            return ok();
        } else {
            return notFound();
        }
    }

    public Result getTypes() {
        return ok(Json.toJson(TextelementType.values()));
    }
}
