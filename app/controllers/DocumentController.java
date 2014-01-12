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
import models.Textelement;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import scala.annotation.elidable;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * The main set of web services.
 */
@Named
@Singleton
public class DocumentController extends Controller {

    private final DocumentRepository documentRepository;
    private final ParagraphRepository paragraphRepository;
    private final HeadlineRepository headlineRepository;

    // We are using constructor injection to receive a repository to support our desire for immutability.
    @Inject
    public DocumentController(final DocumentRepository documentRepository, ParagraphRepository paragraphRepository,
            HeadlineRepository headlineRepository) {
        this.documentRepository = documentRepository;
        this.paragraphRepository = paragraphRepository;
        this.headlineRepository = headlineRepository;
    }

    public Result saveDoc() {
        JsonNode json = request().body().asJson();
        if (json != null) {
            Form<Document> docForm = Form.form(Document.class);
            
            // das führt leider zu einer Exception kriegt man aber evtl hin?
//            docForm.bind(json);
            Long id = json.get("id").asLong();
            Document doc = documentRepository.findOne(id);
            doc.changedAt = new Date();
            
            Form<Headline> headlineForm = Form.form(Headline.class);
            Form<Paragraph> paraForm = Form.form(Paragraph.class);
            
            JsonNode textelements = json.get("textelements");
            for (JsonNode textelement : textelements) {
                if ("Paragraph".equals(textelement.get("type").asText())) {
                    Logger.info("saving Para ", textelement.toString());
                    Paragraph para = paraForm.bind(textelement).get();
                    if (para.id != null) {
                        Paragraph dbPara = paragraphRepository.findOne(para.id);
                        dbPara.merge(para);
                        paragraphRepository.save(dbPara);
                    } else {
                        paragraphRepository.save(para);
                    }
                    doc.textelements.add(para);
                } else {
                    Logger.info("saving Headline ", textelement.toString());
                    Headline headline = headlineForm.bind(textelement).get();
                    if (headline.id != null) {
                        Headline dbHeadline = headlineRepository.findOne(headline.id);
                        dbHeadline.merge(headline);
                        headlineRepository.save(dbHeadline);
                    } else {
                        headlineRepository.save(headline);
                    }
                    headlineRepository.save(headline);
                    doc.textelements.add(headline);
                } 
                
            }
            documentRepository.save(doc );
            return ok(Json.toJson(doc));
        }
        return badRequest("Expecting Json data");
    }

    public Result findById(Long id) {
        Document doc = documentRepository.findOne(id);
        return ok(Json.toJson(doc));
    }
}
