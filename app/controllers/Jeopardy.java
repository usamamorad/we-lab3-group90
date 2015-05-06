package controllers;

import at.ac.tuwien.big.we15.lab2.api.*;
import at.ac.tuwien.big.we15.lab2.api.impl.PlayJeopardyFactory;
import at.ac.tuwien.big.we15.lab2.api.impl.SimpleJeopardyGame;
import play.Logger;
import play.cache.Cache;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import models.*;
import views.html.jeopardy;
import views.html.question;

/**
 * Created by Andreea on 04/5/2015.
 */
@Security.Authenticated(Secured.class)
public class Jeopardy extends Controller{

        public static JeopardyGame current_game;


    public static Result loadGame() {
        //JeopardyFactory factory = new PlayJeopardyFactory("/conf/data.de.json");
        //JeopardyGame game = factory.createGame(Secured.getAuthentication(session()));
        JeopardyGame game = getCachedGame();
        return ok(jeopardy.render(game));
    }

    //human chooses category and clicks waehlen
    public static Result chooseQuestionCategory(){

        DynamicForm categoryForm = Form.form().bindFromRequest();
        int id =Integer.parseInt(categoryForm.data().get("question_selection"));

        JeopardyGame current_game = getCachedGame();
        current_game.chooseHumanQuestion(id);
        System.out.println(current_game.getHumanPlayer().getChosenQuestion().getId()); //passt -> wird richtig gesetzt



        return redirect(routes.Jeopardy.loadQuestion());
    }

    private int getCurrentRoundNr(SimpleJeopardyGame game){
        int human_has_answered=game.getHumanPlayer().getAnsweredQuestions().size();
        int computer_has_answered=game.getMarvinPlayer().getAnsweredQuestions().size();

        if(computer_has_answered>human_has_answered){ //human has not answered yet this round
            return human_has_answered;
        }
        else{ return computer_has_answered;}
            }


    //------------------QUESTION PAGE-------------

    public static Result loadQuestion(){
        JeopardyGame current_game = getCachedGame();

        System.out.println("QUESTION PAGE id: "+current_game.getHumanPlayer().getChosenQuestion().getText());
        return ok(question.render(current_game));
    }




    //private methoden aus Application

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
        Cache.remove(Secured.getAuthentication(session()) + "_game");
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

    /**
     * add current game state to cache
     * @param game
     */
    private static void cachegame(JeopardyGame game){
        Cache.set(Secured.getAuthentication(session()) + "_game", game, 3600);
    }
}
