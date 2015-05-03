package controllers;

import models.Benutzer;
import play.mvc.*;

import views.html.*;

import javax.persistence.TypedQuery;
import java.util.List;

public class Application extends Controller {

//    public static Result authentication() {
//        return ok(authentication.render("Jeopardy!"));



    public static Result index() {
        return ok(authentication.render("Jeopardy!",null));
    }

    @play.db.jpa.Transactional
    public static Result viewBenutzer() {
        TypedQuery<Benutzer> q = play.db.jpa.JPA.em().createQuery("SELECT b FROM Benutzer b", Benutzer.class);
        List<Benutzer> list = (List<Benutzer>) q.getResultList();

        return ok(viewBenutzer.render(list));
    }
}
