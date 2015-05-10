package controllers;

import at.ac.tuwien.big.we15.lab2.api.*;
import at.ac.tuwien.big.we15.lab2.api.impl.PlayJeopardyFactory;

import play.cache.Cache;
import play.data.DynamicForm;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import models.*;
import views.html.jeopardy;
import views.html.question;
import views.html.winner;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Security.Authenticated(Secured.class)
public class Jeopardy extends Controller{

    @Security.Authenticated(Secured.class)
    public static Result loadGame() {
        JeopardyGame game = getCachedGame();
        return ok(jeopardy.render(game));
    }

    //human chooses category and clicks waehlen
    @Security.Authenticated(Secured.class)
    public static Result chooseQuestionCategory(){

        DynamicForm categoryForm = Form.form().bindFromRequest();
        int id =Integer.parseInt(categoryForm.data().get("question_selection"));

        JeopardyGame current_game = getCachedGame();
        current_game.chooseHumanQuestion(id);

        List<Answer> answers = getRandomisedAnswers(current_game.getHumanPlayer().getChosenQuestion());

        return ok(question.render(current_game, answers));

    }

    //Question Page
    @Security.Authenticated(Secured.class)
    public static Result answerQuestion(){
        QuestionForm questionForm = Form.form(QuestionForm.class).bindFromRequest().get();

        JeopardyGame game = getCachedGame();
        game.answerHumanQuestion(questionForm.getAnswers());

        if(game.isGameOver()){
            return ok(winner.render(game));
        }
        else{
            return ok(jeopardy.render(game));
        }
    }

    @Security.Authenticated(Secured.class)
    public static Result nextGame(){
        JeopardyGame game = initGame();

        return ok(jeopardy.render(game));
    }


    /**
     * Gets a List of 4 Answers to show on Question Page for this Question
     * @param q: Chosen Question of Human Player
     * @return List of exactly 4 answers to display
     */
    private static List<Answer> getRandomisedAnswers(Question q){

        if(q==null)
            return null;

        int rnd = ((int)(Math.random()*q.getCorrectAnswers().size()))%3+1;

        List<Answer> answers = new ArrayList<>();
        List<Answer> correct = q.getCorrectAnswers();
        List<Answer> wrong   = new ArrayList<>(q.getAllAnswers());
        wrong.removeAll(q.getCorrectAnswers());

        //add correct answers
        int x=0;
        while(x < rnd){
            answers.add(correct.get(x));
            x++;
        }
        //add wrong answers
        for(int i=0; i<4-(rnd) && i<wrong.size(); i++){
            answers.add(wrong.get(i));
        }

        //auffuellen mit richtigen wenn falsche ausgegangen sind
        if(answers.size() < 4){
            while(x < correct.size() && answers.size() < 4) {
                answers.add(correct.get(x));
                x++;
            }
        }

        //gut mischen
        Collections.shuffle(answers);
        return answers;
    }


    /**
     * creates a new Game
     * @return new JeoPardyGame
     */
    @Security.Authenticated(Secured.class)
    private static JeopardyGame initGame(){

        Benutzer benutzer = getCachedBenutzer();
        if(benutzer == null){
            return null;
        }

        JeopardyGame game = createNewGame(benutzer);

        if(game == null) {
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
        cacheGame(game);

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
    private static void cacheGame(JeopardyGame game){
        Cache.remove(Secured.getAuthentication(session()) + "_game");
        Cache.set(Secured.getAuthentication(session()) + "_game", game, 3600);
    }
}
