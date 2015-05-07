package controllers;

import java.io.File;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import models.Document;
import models.DocumentRepository;
import play.Logger;
import play.mvc.Controller;
import play.mvc.Http.MultipartFormData;
import play.mvc.Http.MultipartFormData.FilePart;
import play.mvc.Result;
import util.Importer;

/**
 * The main set of web services.
 */
@Named
@Singleton
public class ImportController extends Controller {

    private final DocumentRepository documentRepository;

    // We are using constructor injection to receive a repository to support our desire for immutability.
    @Inject
    public ImportController(final DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Result form() {
        return ok(views.html.importer.render(""));
    }

    public Result upload() {

        MultipartFormData body = request().body().asMultipartFormData();
        FilePart filePart = body.getFile("file");
        if (filePart != null) {
            String fileName = filePart.getFilename();
//            String contentType = filePart.getContentType();
            File file = filePart.getFile();

            try {
                Map<String, String[]> postData = body.asFormUrlEncoded();
                String encoding = getEncoding(postData.get("encoding"));
                Document doc = Importer.importFromTextfile(file, encoding);
                doc.title = fileName;
                doc.givenname = "Vorname";
                doc.surname = "Nachname";
                documentRepository.save(doc);
                return redirect(routes.Application.findById(doc.id));
            } catch (Exception e) {
                Logger.warn("Datei liegt nicht im richtigen Format vor.", e);
                flash("error", "Datei liegt nicht im richtigen Format vor.");
                return redirect(routes.ImportController.form());
            }

        } else {
            flash("error", "Missing file");
            return redirect(routes.ImportController.form());
        }

    }

    private static String getEncoding(String[] encoding) {
        if (encoding.length == 1) {
            if ("UTF-8".equals(encoding[0])) {
                return "UTF-8";
            } else {
                return "ISO-8859-15";
            }
        } else {
            return "ISO-8859-15";
        }
    }

}
