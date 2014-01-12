package controllers;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import models.Headline;
import models.Paragraph;
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

    // We are using constructor injection to receive a repository to support our desire for immutability.
    @Inject
    public TextelementController(TextelementRepository textelementRepository) {
        this.textelementRepository = textelementRepository;
    }

    public Result newParagraph() {
        Paragraph para = new Paragraph();
        para.text = "Neuer Paragraph";
        return ok(Json.toJson(para));
    }

    public Result newHeadline() {
        Headline headline = new Headline();
        headline.text = "Neue Headline";
        headline.size = 3;
        return ok(Json.toJson(headline));
    }

    public Result delete(Long id) {
        textelementRepository.delete(id);
        return ok();
    }
}
