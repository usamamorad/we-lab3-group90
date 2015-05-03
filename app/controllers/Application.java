package controllers;

import at.ac.tuwien.big.we15.lab2.api.JeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.JeopardyGame;
import at.ac.tuwien.big.we15.lab2.api.impl.PlayJeopardyFactory;
import models.Benutzer;
import play.Logger;
import play.cache.Cache;
import play.mvc.*;

import views.html.*;

@Security.Authenticated(Secured.class)
public class Application extends Controller {


    @Security.Authenticated(Secured.class)
    public static Result jeopardy() {
        return ok(jeopardy.render(getCachedGame()));
    }

    @Security.Authenticated(Secured.class)
    public static Result question() {
        return ok(question.render(getCachedGame()));
    }

    /**
     * creates a new Game
     * @return new JeoPardyGame
     */
    @Security.Authenticated(Secured.class)
    private static JeopardyGame initGame(){

        Benutzer benutzer = getCachedBenutzer();
        if(benutzer == null){
            Logger.debug("problem! user not set in cache");
            return null;
        }

        JeopardyGame game = createNewGame(benutzer);

        if(game == null) {
            Logger.debug("problem! game=null");
            return null;
        }

        return game;

    }

    /**
     * creates a new Game with the correct Language
     * @return new JeoPardyGame
     */
    private static JeopardyGame createNewGame(Benutzer benutzer){
        String language = lang().language();

        JeopardyFactory factory;

        if(language.equals("de")){
            factory = new PlayJeopardyFactory("data.de.json");
        }else{
            factory = new PlayJeopardyFactory("data.en.json");
        }

        JeopardyGame game = factory.createGame(benutzer);

        if(game == null){
            return null;
        }

        //add game Object to cache
        Cache.remove(Secured.getAuthentication(session())+"_game");
        Cache.set(Secured.getAuthentication(session()) + "_game", game, 3600);

        return game;
    }

    /**
     * Vorbedingung: wird von einer @Security.Authenticated methode aufgerufen
     * @return der Benutzer im cache sonst null
     */
    private static Benutzer getCachedBenutzer(){
        if(Cache.get(Secured.getAuthentication(session()) + "_user") == null){
            return null;
        }
        return (Benutzer) Cache.get(Secured.getAuthentication(session())+"_user");
    }

    /**
     * Vorbedingung: wird von einer @Security.Authenticated methode aufgerufen
     * @return das GameObject im cache sonst ein neues Spiel
     */
    private static JeopardyGame getCachedGame(){
        if(Cache.get(Secured.getAuthentication(session())+"_game") == null){
            //create game
            return initGame();
        }
        return (JeopardyGame) Cache.get(Secured.getAuthentication(session())+"_game");
    }

}
