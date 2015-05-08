package controllers;

import models.Benutzer;
import play.cache.Cache;
import play.data.DynamicForm;
import play.data.Form;
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

    public static Result index() {
        return ok(authentication.render(null));
    }

    public static Result login() {
        return redirect("/");
    }

    public static Result logout() {

        Cache.remove(Secured.getAuthentication(session()) + "_game");
        Cache.remove(Secured.getAuthentication(session()) + "_user");

        session().clear();
        return redirect("/");
    }

    @play.db.jpa.Transactional(readOnly = true)
    public static Result authenticateUser() {

        DynamicForm loginForm = Form.form().bindFromRequest();

        String user = getUserName(loginForm);
        String pass = getPassword(loginForm);

        System.out.println(user + " "+ pass);

        validateInput(loginForm,user,pass);
        if(loginForm.hasErrors()){
            System.out.println("empty input");
            loginForm.reject(loginForm.globalError());
            return badRequest(authentication.render(null));
        }else {
        
        boolean userOK = checkUser(user, pass);
        System.out.println(userOK);
        if (userOK) {
            System.out.println("logged in as valid user!");
            return redirect(routes.Jeopardy.loadGame());
        }
        else {
            System.out.println("login failed - user not ok");
            loginForm.reject("authentication unsuccessful");
            return badRequest(authentication.render("Authentication error!"));
        }

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
            //add authentication for the User in the HTTP session
            Secured.addAuthentication(session(), user);
            //add user Object in cache
            Cache.set(Secured.getAuthentication(session())+"_user", user, 3600);
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

    private static void validateInput(Form form,String username, String password){

        if (username==null || password==null){
            form.reject("Fields cannot be empty");
        }
    }

}
