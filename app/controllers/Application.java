package controllers;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

import play.mvc.Controller;
import play.mvc.Result;

/**
 * The main set of web services.
 */
@Named
@Singleton
public class Application extends Controller {


    // We are using constructor injection to receive a repository to support our desire for immutability.
//    @Inject
//    public Application() {
//    }

    public Result index() {
        return ok(views.html.index.render(""));
    }
    
    public Result newDoc(){
        return ok(views.html.document.render("new"));
    }
    
    public Result findById(Long id){
        return ok(views.html.document.render("" + id));
    }
}
