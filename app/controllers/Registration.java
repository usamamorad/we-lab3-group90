package controllers;

import models.Benutzer;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.authentication;
import views.html.registration;

import java.time.LocalDate;

public class Registration extends Controller {


    public static Result register() {
        return ok(registration.render("Jeopardy!"));
    }

    @Transactional
    public static Result registerUser() {

        DynamicForm form = Form.form().bindFromRequest();
        Benutzer benutzer = new Benutzer();

        if(form.hasErrors()){
            return badRequest(registration.render("Schlechtes Input!"));
        }
        else {
            benutzer.setFirstName(form.data().get("firstname"));
            benutzer.setLastName(form.data().get("lastname"));

            //TODO validate birthday
            if (!form.data().get("birthdate").equals("")) {
                benutzer.setBirthday(LocalDate.parse(form.data().get("birthdate")));
            }
            benutzer.setGender(form.data().get("gender"));
            benutzer.setAvatarId(form.data().get("avatar"));
            benutzer.setName(form.data().get("username"));
            benutzer.setPassword(form.data().get("password"));

            JPA.em().persist(benutzer);
            //return ok("Hallo"+benutzer.getName());
            return ok(authentication.render("Jeopardy!",null));
        }
    }
}
