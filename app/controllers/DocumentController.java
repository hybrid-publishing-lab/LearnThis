package controllers;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import models.Document;
import models.DocumentRepository;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

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
        // documentRepository.save();
        return ok();
    }

    public Result newDoc() {
        final Document doc = new Document();
        documentRepository.save(doc);
        return ok(Json.toJson(doc));
    }

    public Result findById(Long id) {
        Document doc = documentRepository.findOne(id);
        return ok(Json.toJson(doc));
    }
}
