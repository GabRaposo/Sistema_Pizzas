package br.edu.ufersa.PizzariaDAOeSERVICE;

import br.edu.ufersa.PizzariaDAOeSERVICE.view.NavigationManager;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {

    public void start(Stage primaryStage) {
        primaryStage.setTitle("Papa's Pizzeria");
        NavigationManager.getInstance().iniciar(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
