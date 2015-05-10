package controllers;

import models.Benutzer;
import play.mvc.Http.Context;
import play.mvc.Http.Session;
import play.mvc.Result;
import play.mvc.Security;

public class Secured extends Security.Authenticator {

    public static void addAuthentication(Session session, Benutzer user) {
        session.clear();
        session.put("userId", user.getName());
    }

    public static String getAuthentication(Session session) {
        return session.get("userId");
    }

    @Override
    public String getUsername(Context ctx) {
        return ctx.session().get("userId");
    }

    @Override
    public Result onUnauthorized(Context ctx) {
        return redirect(routes.Authentication.logout());
    }
}