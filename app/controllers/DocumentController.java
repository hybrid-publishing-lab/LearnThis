package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import models.Document;
import models.DocumentRepository;
import models.Headline;
import models.Paragraph;
import play.Logger;
import play.data.Form;
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
public class DocumentController extends Controller {

    private final DocumentRepository documentRepository;

    // We are using constructor injection to receive a repository to support our desire for immutability.
    @Inject
    public DocumentController(final DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Result saveDoc() {
        JsonNode json = request().body().asJson();
        if (json != null) {

            // TODO das führt leider zu einer Exception kriegt man aber evtl hin?
            // Form<Document> docForm = Form.form(Document.class);
            // docForm.bind(json);
            Long id = json.get("id").asLong();
            Document doc = documentRepository.findOne(id);
            doc.changedAt = new Date();
            doc.title = json.get("title").asText();
            doc.surname = json.get("surname").asText();
            doc.givenname = json.get("givenname").asText();

            Form<Headline> headlineForm = Form.form(Headline.class);
            Form<Paragraph> paraForm = Form.form(Paragraph.class);

            JsonNode textelements = json.get("textelements");
            doc.textelements.clear();
            int i = 0;
            for (JsonNode textelement : textelements) {
                if ("Paragraph".equals(textelement.get("type").asText())) {
                    Logger.info("saving Para ", textelement.toString());
                    Paragraph para = paraForm.bind(textelement).get();
                    para.document = doc;
                    para.sort = i;
                    para.updateKeywords();
                    doc.textelements.add(para);
                } else {
                    Logger.info("saving Headline", textelement.toString());
                    Headline headline = headlineForm.bind(textelement).get();
                    headline.document = doc;
                    headline.sort = i;
                    headline.updateKeywords();
                    doc.textelements.add(headline);
                }
                i++;
            }
            documentRepository.save(doc);
            return ok(Json.toJson(doc));
        }
        return badRequest("Expecting Json data");
    }

    public Result delete(Long id) {
        documentRepository.delete(id);
        return ok();
    }

    public Result findById(Long id) {
        Document doc = documentRepository.findOne(id);
        JpaFixer.removeDuplicatesWorkaround(doc);
        return ok(Json.toJson(doc));
    }

    public Result findAll() {
        Iterable<Document> docs = documentRepository.findAll();
        List<Document> result = new ArrayList<Document>();
        for (Document doc : docs) {
            result.add(doc);
        }
        JpaFixer.removeDuplicatesWorkaround(result);
        return ok(Json.toJson(result));
    }
}
