package controllers;

import play.*;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    public static Result authentication() {
        return ok(authentication.render("Jeopardy!"));
    }

//    public static Result register() {
//        return ok(registration.render("Jeopardy!"));
//    }
}
