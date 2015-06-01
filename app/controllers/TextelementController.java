package controllers;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import models.Card;
import models.Document;
import models.DocumentRepository;
import models.Headline;
import models.MultipleChoice;
import models.Paragraph;
import models.TextelementType;

import org.apache.commons.lang3.StringUtils;

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
            Card card = new Card();
            card.front = para;
            doc.insertCard(card, index);
            documentRepository.save(doc);
            return ok(Json.toJson(card));
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
            Card card = new Card();
            card.front = headline;
            doc.insertCard(card, index);
            documentRepository.save(doc);
            return ok(Json.toJson(card));
        } else {
            return notFound();
        }
    }

    public Result newMultipleChoice(Long id, Integer index) {
        Document doc = documentRepository.findOne(id);

        JsonNode json = request().body().asJson();
        MultipleChoice mc = new MultipleChoice();
        MultipleChoice mc2 = new MultipleChoice();
        mc.text = "Neue Karte";
        mc2.text = "Rückseite";
        if (json != null) {
            JsonNode front = json.get("front");
            if (front != null && !(front instanceof NullNode)) {
                mc.text = front.asText();
            }
            JsonNode back = json.get("back");
            if (back != null && !(back instanceof NullNode)) {
                mc2.text = back.asText();
            }
        }
        Card card = new Card();
        card.front = mc;
        card.back = mc2;
        
//        if (doc != null) {
//            JpaFixer.removeDuplicatesWorkaround(doc);
//            doc.insertCard(card, index);
//            doc = documentRepository.save(doc);
//            return ok(Json.toJson(doc.cards.get(index)));
//        } else {
            return ok(Json.toJson(card));
//        }
    }

    public Result delete(Long docId, Long cardId) {
        Document doc = documentRepository.findOne(docId);
        if (doc != null) {
            JpaFixer.removeDuplicatesWorkaround(doc);
            boolean removed = doc.removeCard(cardId);
            documentRepository.save(doc);
            if (removed) {
                return ok();
            }
        }
        return notFound();
    }

    public Result getTypes() {
        return ok(Json.toJson(TextelementType.values()));
    }
}
