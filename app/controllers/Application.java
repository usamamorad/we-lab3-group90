package controllers;

import at.ac.tuwien.big.we15.lab2.api.JeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.JeopardyGame;
import at.ac.tuwien.big.we15.lab2.api.impl.PlayJeopardyFactory;
import models.Benutzer;
import play.cache.Cache;
import play.mvc.*;

import views.html.*;

public class Application extends Controller {

    /**
     * shows LoginPage
     * @return
     */
    public static Result index() {
        return ok(authentication.render("Jeopardy!"));
    }

    /**
     * creates a new Game if User is logged in
     * @return new JeoPardyGame
     */
    public static Result initGame(){
        //get user from cache
        Benutzer user = (Benutzer) Cache.get("user");

        //no user found go to login page
        if(user == null){
            return redirect("/");
        }

        JeopardyGame game = createNewGame(user);

        if(game == null) {
            return badRequest(authentication.render("Error in establishing the Game!"));
        }

        //Save GameData in Cache
        Cache.set("game", game);

        return ok(jeopardy.render(game));
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

        return factory.createGame(benutzer);
    }

    public static Result jeopardy() {
        return ok();
    }
}
