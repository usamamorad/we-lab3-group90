package controllers;

import models.Benutzer;
import play.cache.Cache;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.JPA;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.authentication;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;


/**
 * Created by Andreea on 01/5/2015.
 */

public class Authentication extends Controller {

    public static Result authentication() {
        return ok(authentication.render("Jeopardy!"));
    }

    public static Result login() {
        return redirect("/index");
    }

    public static Result logout() {
        session().clear();
        return redirect("/index");
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result authenticateUser() {

        DynamicForm loginForm = Form.form().bindFromRequest();

        String user = getUserName(loginForm);
        String pass = getPassword(loginForm);

        System.out.println(user + " "+ pass);

        boolean userOK = checkUser(user, pass);
        System.out.println(userOK);
        if (userOK) {
            System.out.println("logged in as valid user!");
            return redirect("/initGame");
        }
        else{
            System.out.println("login failed - user not ok");
            loginForm.reject("authentication unsuccessful");
            return badRequest(authentication.render("Jeopardy"));


        }

    }

    private static String getPassword(DynamicForm loginForm) {
        return loginForm.data().get("password");
    }

    private static String getUserName(DynamicForm loginForm) {
        return loginForm.data().get("username");
    }

    private static boolean checkUser(String username, String password){
        Benutzer user = findUserByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            Cache.set("user", user);
            return true;
        }
        return false;
    }

    private static Benutzer findUserByUsername(String username){
        EntityManager em = play.db.jpa.JPA.em();
        String queryStr = " select b from Benutzer b where b.name = :username";
        TypedQuery<Benutzer> query = em.createQuery(queryStr,Benutzer.class).setParameter("username", username);
       // TypedQuery<Benutzer> query = em.createQuery(queryStr,Benutzer.class);
        List<Benutzer> list = query.getResultList();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

}
