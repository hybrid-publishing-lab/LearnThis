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

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.JpaFixer;
import util.MailUtil;

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
            Document doc = null;
            if (id == -1L) {
                doc = new Document();
            } else {
                doc = documentRepository.findOne(id);
            }
            
            if (doc.password != null && doc.password.length() > 0 && !doc.password.equals(pw)) {
                return badRequest("Invalid password");
            }
            
            doc.changedAt = new Date();
            doc.title = json.get("title").asText();
            doc.surname = json.get("surname").asText();
            doc.givenname = json.get("givenname").asText();
            doc.description = json.get("description").asText();
            doc.email = json.get("email").asText();

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
                    try {
                        c.back = buildTextelement(textelement);
                    } catch (Exception e) {
                        Logger.error("Error while building Textelement for back", e);
                    }
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
        try {
            Form<MultipleChoice> mcForm = Form.form(MultipleChoice.class);
            Form<Headline> headlineForm = Form.form(Headline.class);
            Form<Paragraph> paraForm = Form.form(Paragraph.class);
            
            if ("Paragraph".equalsIgnoreCase(textelement.get("type").asText())) {
                Logger.info("saving Para ", textelement.toString());
                return paraForm.bind(textelement).get();
            } else if("Headline".equals(textelement.get("type").asText())) {
                Logger.info("saving Headline", textelement.toString());
                return headlineForm.bind(textelement).get();
            } else {
                Logger.info("saving MultipleChoice", textelement.toString());
                return mcForm.bind(textelement).get();
            }
        } catch (Exception e) {
            Logger.error("Error while building Textelement", e);
        }
        return new MultipleChoice();
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

    public Result resetPassword(Long id) {
        Document doc = documentRepository.findOne(id);
        if (StringUtils.isNotBlank(doc.email)) {
//            routes.DocumentController.findById().absoluteURL(request());
            String mailText = "Das Passwort für die Lernkarte \"" + doc.title + "\" lautet: \n"+doc.password;
            MailUtil.sendMail(mailText, "Lernkarten Passwort", doc.email);
            return ok();
        } else {
            
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
        if (id == -1L) {
            // new Document
            return ok("");
        }
        
        String pw = getPwFromRequest();
        Document doc = documentRepository.findOne(id);
        if (doc.password == null || doc.password.length() == 0 || doc.password.equals(pw)) {
            String result = "";
            if (StringUtils.isNotBlank(doc.email)) {
                result += doc.email;
            }
            return ok(result);
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
        if (id == -1L) {
            // new Document
            return ok(Json.toJson(newDocument()));
        } else {
            Document doc = documentRepository.findOne(id);
            JpaFixer.removeDuplicatesWorkaround(doc);
            return ok(Json.toJson(doc));
        }
    }

    private Document newDocument() {
        final Document doc = new Document();
        doc.id = -1L;
        doc.title = "Thema";
        doc.givenname = "Vorname";
        doc.surname = "Nachname";
        doc.description = "";
        doc.createdAt = new Date();
        doc.changedAt = new Date();
        doc.password = "";

        // add paragraph
        MultipleChoice mc = new MultipleChoice();
        MultipleChoice mc2 = new MultipleChoice();
        mc.text = "Neue Karte";
        mc.text = "";
        Card card = new Card();
        card.sort = 1;
        card.front = mc;
        card.back = mc2;
        card.document = doc;
        doc.cards.add(card);
        return doc;
    }

    public Result findAll(Integer page) {
        if (page == null || page < 0) {
            page = 0;
        }
        Pageable pageable = new PageRequest(page, 20);
        Iterable<Document> docs = documentRepository.findByIdNotNullOrderByChangedAtDesc(pageable);
        List<Document> result = new ArrayList<Document>();
        for (Document doc : docs) {
            result.add(doc);
        }
        JpaFixer.removeDuplicatesWorkaround(result);
        return ok(Json.toJson(result));
    }

    public Result findRandom(Integer count) {
        if (count == null || count < 0) {
            count = 5;
        } else if (count > 100) {
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
