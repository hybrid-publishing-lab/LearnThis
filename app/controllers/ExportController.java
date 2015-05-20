package controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import models.Document;
import models.DocumentRepository;
import nl.siegmann.epublib.domain.Book;
import nl.siegmann.epublib.epub.EpubWriter;

import org.apache.commons.io.output.NullOutputStream;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import play.mvc.Controller;
import play.mvc.Result;
import util.JpaFixer;
import util.converter.DocumentConverter;

/**
 * The main set of web services.
 */
@Named
@Singleton
public class ExportController extends Controller {

    private final DocumentRepository documentRepository;

    // We are using constructor injection to receive a repository to support our desire for immutability.
    @Inject
    public ExportController(final DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public Result exportEpub(Long id) {
        try {
            Document doc = documentRepository.findOne(id);
            JpaFixer.removeDuplicatesWorkaround(doc);
            Book book = DocumentConverter.toEpub(doc);

            // Write Epub to Buffer
            EpubWriter epubWriter = new EpubWriter();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            epubWriter.write(book, out);
            byte[] buffer = out.toByteArray();
            
            String filename = doc.title+"-"+doc.givenname+"_"+doc.surname+"-"+id+".epub";
            filename = cleanFileName(filename);
            response().setHeader("Content-disposition","attachment; filename="+filename);
            return ok(new ByteArrayInputStream(buffer)).as("application/epub+zip");
        } catch (Exception e) {
            return notFound();
        }
    }
    
    public Result exportHtml(Long id) {
        Document doc = documentRepository.findOne(id);
        JpaFixer.removeDuplicatesWorkaround(doc);
        
        String filename = doc.title+"-"+doc.givenname+"_"+doc.surname+"-"+id+".html";
        filename = cleanFileName(filename);
        response().setHeader("Content-Type", "text/html; charset=utf-8");
        response().setHeader("Content-disposition","attachment; filename="+filename);
        return ok(views.html.documentExport.render(doc));
    }
    
    public Result exportPdf(Long id) {
        try {
            Document doc = documentRepository.findOne(id);
            JpaFixer.removeDuplicatesWorkaround(doc);
           
            ITextRenderer renderer = new ITextRenderer();
            OutputStream devnull = new NullOutputStream();
            PrintWriter devnullWriter = new PrintWriter(devnull);
            String url = "http://localhost:9000/export/html/document/"+id;
            InputStream is = new URL(url).openStream();

            Tidy tidy = new Tidy();
            tidy.setErrout(devnullWriter);
            tidy.setInputEncoding("UTF-8");
            tidy.setMakeClean(true);

            org.w3c.dom.Document webDocument = tidy.parseDOM(is, devnull);

            renderer.setDocument(webDocument, url);
            renderer.layout();
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            renderer.createPDF(out);

            // Write pdf to Buffer
            byte[] buffer = out.toByteArray();
            
//            response().setContentType("application/x-download");
            String filename = doc.title+"-"+doc.givenname+"_"+doc.surname+"-"+id+".pdf";
            filename = cleanFileName(filename);
            response().setHeader("Content-disposition","attachment; filename="+filename);
            return ok(new ByteArrayInputStream(buffer)).as("application/pdf");
        } catch (Exception e) {
            return notFound();
        }
    }
    
    private String cleanFileName(String filename){
        return filename.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }
}
