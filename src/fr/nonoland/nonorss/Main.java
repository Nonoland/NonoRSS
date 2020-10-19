package fr.nonoland.nonorss;

import fr.nonoland.nonorss.controllers.NewRSSController;
import fr.nonoland.nonorss.controllers.RSSManagementController;
import fr.nonoland.nonorss.controllers.WindowController;
import fr.nonoland.nonorss.utils.LocalSave;
import fr.nonoland.nonorss.utils.RssReader;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Main extends Application {

    /*
    TODO Téléchargement des flux rss asynchrone pour éviter l'attente du chargement de l'application
    TODO Ajout de l'article déjà lu (Juste changer le style)
     */

    public LocalSave localSave;

    public ArrayList<RssReader> fluxRss = new ArrayList<RssReader>();

    /* Controllers */
    private WindowController windowController;
    private NewRSSController newRSSController;
    private RSSManagementController rssManagementController;

    public Stage stageNewRSS;
    public Stage stageRSSManagement;

    public Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        localSave = new LocalSave(this);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Window.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("NonoRSS");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        windowController = loader.getController();
        windowController.setMain(this);

        windowController.updateRSS();
    }

    public void showNewRSSWindows(Stage stageOwner) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewRSS.fxml"));
        Parent root = loader.load();

        stageNewRSS = new Stage();
        stageNewRSS.setTitle("Nouveau Flux RSS");
        stageNewRSS.initModality(Modality.WINDOW_MODAL);
        stageNewRSS.initOwner(stageOwner);
        Scene scene = new Scene(root);
        stageNewRSS.setScene(scene);

        newRSSController = loader.getController();
        newRSSController.setMain(this);

        stageNewRSS.showAndWait();
    }

    public void showRssManagementWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("RSSManagement.fxml"));
        Parent root = loader.load();

        stageRSSManagement = new Stage();
        stageRSSManagement.setTitle("Gestion des flux RSS");
        stageRSSManagement.initModality(Modality.WINDOW_MODAL);
        stageRSSManagement.initOwner(primaryStage);
        Scene scene = new Scene(root);
        stageRSSManagement.setScene(scene);

        rssManagementController = loader.getController();
        rssManagementController.setMain(this);
        rssManagementController.updateListView();

        stageRSSManagement.showAndWait();
    }

    public ArrayList<RssReader> getFluxRss() {
        return this.fluxRss;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
