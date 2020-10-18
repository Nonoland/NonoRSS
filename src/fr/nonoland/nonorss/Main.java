package fr.nonoland.nonorss;

import fr.nonoland.nonorss.controllers.NewRSSController;
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
import java.net.URL;
import java.util.ArrayList;

public class Main extends Application {

    public LocalSave localSave;

    //public RssReader rssReader;

    public ArrayList<RssReader> fluxRss = new ArrayList<RssReader>();

    /* Controllers */
    private WindowController windowController;
    private NewRSSController newRSSController;

    public Stage stageNewRSS;

    private Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;

        localSave = new LocalSave(this);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Window.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("Window.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("NonoRSS");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        URL feedSource = new URL("https://korben.info/feed");
        //this.rssReader = RssReader.getRssReaderWithURL(feedSource);

        windowController = loader.getController();
        windowController.setMain(this);

        windowController.updateRSS();
    }

    public void showNewRSSWindows() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewRSS.fxml"));
        Parent root = loader.load();

        stageNewRSS = new Stage();
        stageNewRSS.setTitle("Nouveau Flux RSS");
        stageNewRSS.initModality(Modality.WINDOW_MODAL);
        stageNewRSS.initOwner(primaryStage);
        Scene scene = new Scene(root);
        stageNewRSS.setScene(scene);

        newRSSController = loader.getController();
        newRSSController.setMain(this);

        stageNewRSS.showAndWait();
    }

    public ArrayList<RssReader> getFluxRss() {
        return this.fluxRss;
    }

    /*public RssReader getRssReader() {
        return this.rssReader;
    }*/

    public static void main(String[] args) {
        launch(args);
    }
}
