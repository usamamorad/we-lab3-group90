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

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.util.List;

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

            //TODO username should not be already used
            if(checkUserByUsername(form.data().get("username"))){
                benutzer.setName(form.data().get("username"));
            }
            else{
                System.out.println("registration failed - username has already been used!");
                return badRequest(registration.render("Benutzername ist schon vorhanden!"));
            }
            benutzer.setPassword(form.data().get("password"));

            JPA.em().persist(benutzer);
            //return ok("Hallo"+benutzer.getName());
            return ok(authentication.render(null));
        }
    }

    /**
     *
     * @param username search to see if is some other user with this username
     * @return true if exists no user with the username = username
     *         false if exists at least 1 user wuth username = username
     */
    private static boolean checkUserByUsername(String username){
        EntityManager em = play.db.jpa.JPA.em();
        String queryStr = " select b from Benutzer b where b.name = :username";
        TypedQuery<Benutzer> query = em.createQuery(queryStr,Benutzer.class).setParameter("username", username);
        // TypedQuery<Benutzer> query = em.createQuery(queryStr,Benutzer.class);
        List<Benutzer> list = query.getResultList();
        if (list.isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}
