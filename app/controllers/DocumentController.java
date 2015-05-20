package controllers;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import models.Card;
import models.Document;
import models.DocumentRepository;
import models.Headline;
import models.MultipleChoice;
import models.Paragraph;
import models.Textelement;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.JpaFixer;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.NullNode;

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
            String pw = getPwFromRequest();

            // TODO das führt leider zu einer Exception kriegt man aber evtl hin?
            // Form<Document> docForm = Form.form(Document.class);
            // docForm.bind(json);
            Long id = json.get("id").asLong();
            Document doc = documentRepository.findOne(id);
            
            if (doc.password != null && doc.password.length() > 0 && !doc.password.equals(pw)) {
                return badRequest("Invalid password");
            }
            
            doc.changedAt = new Date();
            doc.title = json.get("title").asText();
            doc.surname = json.get("surname").asText();
            doc.givenname = json.get("givenname").asText();
            doc.description = json.get("description").asText();

            JsonNode cards = json.get("cards");
            doc.cards.clear();
            int i = 0;
            for (JsonNode card : cards) {
                Card c = new Card();
                c.document = doc;
                c.sort = i;
                JsonNode textelement = card.get("front");
                if (textelement != null && !(textelement instanceof NullNode)) {
                    c.front = buildTextelement(textelement);
                }
                textelement = card.get("back");
                if (textelement != null && !(textelement instanceof NullNode)) {
                    c.back = buildTextelement(textelement);
                }
                doc.cards.add(c);
                c.updateKeywords();
                i++;
            }
            documentRepository.save(doc);
            return ok(Json.toJson(doc));
        }
        return badRequest("Expecting Json data");
    }

    private Textelement buildTextelement(JsonNode textelement) {

        Form<MultipleChoice> mcForm = Form.form(MultipleChoice.class);
        Form<Headline> headlineForm = Form.form(Headline.class);
        Form<Paragraph> paraForm = Form.form(Paragraph.class);
        
        if ("MultipleChoice".equals(textelement.get("type").asText())) {
            Logger.info("saving MultipleChoice", textelement.toString());
            return mcForm.bind(textelement).get();
        } else if("Headline".equals(textelement.get("type").asText())) {
            Logger.info("saving Headline", textelement.toString());
            return headlineForm.bind(textelement).get();
        } else {
            Logger.info("saving Para ", textelement.toString());
            return paraForm.bind(textelement).get();
        }
    }

    public Result changePassword(Long id) {
        String pw = getPwFromRequest();
        Document doc = documentRepository.findOne(id);
        if (doc.password == null || doc.password.equals(pw)) {
            JsonNode json = request().body().asJson();
            JsonNode pwNode = json.get("pwNew");
            String newPassword = null;
            if (pwNode != null && !(pwNode instanceof NullNode)) {
                newPassword = pwNode.textValue();
            }
            doc.password = newPassword;
            documentRepository.save(doc);
            return ok();
        }
        return badRequest();
    }

    public Result delete(Long id) {
        String pw = getPwFromRequest();
        Document doc = documentRepository.findOne(id);
        if (doc.password == null || doc.password.equals(pw)) {
            documentRepository.delete(id);
            return ok();
        }
        return badRequest();
    }

    public Result checkPassword(Long id) {
        String pw = getPwFromRequest();
        Document doc = documentRepository.findOne(id);
        if (doc.password == null || doc.password.length() == 0 || doc.password.equals(pw)) {
            return ok();
        }
        return badRequest();
    }

    private String getPwFromRequest() {
        JsonNode json = request().body().asJson();
        if (json != null) {
            JsonNode pwNode = json.get("pw");
            if (pwNode != null && !(pwNode instanceof NullNode)) {
                return pwNode.textValue();
            }
        }
        return null;
    }

    public Result findById(Long id) {
        Document doc = documentRepository.findOne(id);
        JpaFixer.removeDuplicatesWorkaround(doc);
        return ok(Json.toJson(doc));
    }

    public Result findAll() {
        Iterable<Document> docs = documentRepository.findByIdNotNullOrderByVisitsDesc();
        List<Document> result = new ArrayList<Document>();
        for (Document doc : docs) {
            result.add(doc);
        }
        JpaFixer.removeDuplicatesWorkaround(result);
        return ok(Json.toJson(result));
    }

    public Result findRandom(Integer count) {
        if (count > 100) {
            count = 100;
        }
        Pageable page = new PageRequest(0, count);
        Iterable<Document> docs = documentRepository.findRandom(page);
        List<Document> result = new ArrayList<Document>();
        for (Document doc : docs) {
            result.add(doc);
        }
        JpaFixer.removeDuplicatesWorkaround(result);
        return ok(Json.toJson(result));
    }
}
