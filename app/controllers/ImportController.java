package controllers;

import java.io.File;
import java.util.Map;

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
import models.TextelementRepository;
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
    private final ParagraphRepository paragraphRepository;
    private final HeadlineRepository headlineRepository;
    private final TextelementRepository textelementRepository;

    // We are using constructor injection to receive a repository to support our desire for immutability.
    @Inject
    public ImportController(final DocumentRepository documentRepository, ParagraphRepository paragraphRepository,
            HeadlineRepository headlineRepository, TextelementRepository textelementRepository) {
        this.documentRepository = documentRepository;
        this.paragraphRepository = paragraphRepository;
        this.headlineRepository = headlineRepository;
        this.textelementRepository = textelementRepository;
    }

    public Result form() {
        return ok(views.html.importer.render(""));
    }

    public Result upload() {

        MultipartFormData body = request().body().asMultipartFormData();
        FilePart filePart = body.getFile("file");
        if (filePart != null) {
            String fileName = filePart.getFilename();
            String contentType = filePart.getContentType();
            File file = filePart.getFile();

            try {
                Map<String, String[]> postData = body.asFormUrlEncoded();
                String encoding = getEncoding(postData.get("encoding"));
                Document doc = Importer.importFromTextfile(file, encoding);
                // TODO not very nice to iterate over the textelements
                // TODO not working !!! mit Janus besprechen
                // wenn man es nicht tut, wird eine exception geworfen, dass die elemente noch nicht gespeichert sind.
//                for(Textelement element : doc.textelements){
//                    if(element instanceof Paragraph){
//                        paragraphRepository.save((Paragraph)element);
//                    }else if(element instanceof Headline){
//                        headlineRepository.save((Headline)element);
//                    }
//                }
                documentRepository.save(doc);
                return ok(views.html.document.render(doc));
            } catch (Exception e) {
                Logger.warn("Datei liegt nicht im richtigen Format vor.", e);
                flash("error", "Datei liegt nicht im richtigen Format vor.");
                return form();
                // TODO redirect to Form
                // return redirect(routes.ImportController.form());
            }
            
        } else {
            flash("error", "Missing file");
            return form();
            // TODO redirect to Form
            // return redirect(routes.ImportController.form());
        }

    }
    
    private static String getEncoding(String[] encoding){
        if(encoding.length == 1){
            if("UTF-8".equals(encoding[0])) {
                return "UTF-8";
            }else{
                return "ISO-8859-15";
            }
        }else{
            return "ISO-8859-15";
        }
    }

}
