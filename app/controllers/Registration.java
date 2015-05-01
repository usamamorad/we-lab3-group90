package controllers;

import at.ac.tuwien.big.we15.lab2.api.Avatar;
import models.Benutzer;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.authentication;
import views.html.registration;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class Registration extends Controller {

//    public static Result authentication() {
//        return ok(authentication.render("Jeopardy!"));
//    }


    public static Result register() {
        return ok(registration.render("Jeopardy!"));
   }

    public static Result registerUser() {

        DynamicForm form = Form.form().bindFromRequest();
        Benutzer benutzer = new Benutzer();

        benutzer.setFirstName(form.data().get("firstname"));
        benutzer.setLastName(form.data().get("lastname"));

        //TODO validate birthday
        if(form.data().get("birthdate") != "") {
            benutzer.setBirthday(LocalDate.parse(form.data().get("birthdate")));
        }
        benutzer.setGender(form.data().get("gender"));
        benutzer.setAvatar(Avatar.getAvatar(form.data().get("avatar")));

        //TODO check if unique and length>4 and not null
        benutzer.setName(form.data().get("username"));

        //TODO check lenghth>4 and not null
        benutzer.setPassword(form.data().get("password"));

        //return ok("Hallo"+benutzer.getName());
        return ok(authentication.render("Jeopardy!"));
    }
}
