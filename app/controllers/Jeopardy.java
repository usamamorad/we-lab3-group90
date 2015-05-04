package controllers;

import at.ac.tuwien.big.we15.lab2.api.*;
import at.ac.tuwien.big.we15.lab2.api.impl.PlayJeopardyFactory;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import models.*;
import views.html.jeopardy;

/**
 * Created by Andreea on 04/5/2015.
 */
@Security.Authenticated(Secured.class)
public class Jeopardy extends Controller{


    public static Result newGame() {
        JeopardyFactory factory = new PlayJeopardyFactory("/conf/data.de.json");
        JeopardyGame game = factory.createGame(Secured.getAuthentication(session()));

        return ok(jeopardy.render(game));
    }
}
