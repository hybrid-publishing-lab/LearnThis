package controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import models.Document;
import models.DocumentRepository;
import models.Headline;
import models.HeadlineRepository;
import models.Paragraph;
import models.ParagraphRepository;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubWriter;
import play.Logger;
import play.data.Form;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import util.converter.DocumentConverter;

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

            // das führt leider zu einer Exception kriegt man aber evtl hin?
            // Form<Document> docForm = Form.form(Document.class);
            // TODO nicht sehr schoen, Vielleicht kann Janus eine Idee liefern?
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
            int i = 0;
            for (JsonNode textelement : textelements) {
                if ("Paragraph".equals(textelement.get("type").asText())) {
                    Logger.info("saving Para ", textelement.toString());
                    Paragraph para = paraForm.bind(textelement).get();
                    if (para.id != null) {
                        Paragraph dbPara = paragraphRepository.findOne(para.id);
                        dbPara.merge(para);
                        doc.textelements.add(dbPara);
                    }else{
                        doc.textelements.add(para);
                    }
                } else {
                    Logger.info("saving Headline ", textelement.toString());
                    Headline headline = headlineForm.bind(textelement).get();
                    if (headline.id != null) {
                        Headline dbHeadline = headlineRepository.findOne(headline.id);
                        dbHeadline.merge(headline);
                        doc.textelements.add(dbHeadline);
                    } else{
                        doc.textelements.add(headline);
                    }
                }

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
        return ok(Json.toJson(doc));
    }
    
    public Result findAll() {
        Iterable<Document> docs = documentRepository.findAll();
        List<Document> result = new ArrayList<Document>();
        for(Document doc : docs){
            result.add(doc);
        }
        return ok(Json.toJson(result));
    }

    public Result exportEpub(Long id) {
        try {
            Document doc = documentRepository.findOne(id);
            Book book = DocumentConverter.toEpub(doc);

            // Write Epub to Buffer
            EpubWriter epubWriter = new EpubWriter();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            epubWriter.write(book, out);
            byte[] buffer = out.toByteArray();
            
//            response().setContentType("application/x-download");
            String filename = doc.title+"-"+doc.givenname+"_"+doc.surname+"-"+id+".epub";
            response().setHeader("Content-disposition","attachment; filename="+filename);
            return ok(new ByteArrayInputStream(buffer)).as("application/epub+zip");
        } catch (Exception e) {
            return notFound();
        }
    }
}
