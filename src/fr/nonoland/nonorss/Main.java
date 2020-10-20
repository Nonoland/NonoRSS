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

    private LocalSave localSave;

    private ArrayList<RssReader> fluxRss = new ArrayList<>();

    /* Controllers */
    public WindowController ControllerWindow;
    public NewRSSController ControllerNewRSS;
    public RSSManagementController ControllerRSSManagement;

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

        ControllerWindow = loader.getController();
        ControllerWindow.setMain(this);

        ControllerWindow.updateRSS();
    }

    /*
    Affiche une fenetre modal: NewRSS.fxml
     */
    public void showWindowNewRSS(Stage stageOwner) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("NewRSS.fxml"));
        Parent root = loader.load();

        stageNewRSS = new Stage();
        stageNewRSS.setTitle("Nouveau Flux RSS");
        stageNewRSS.initModality(Modality.WINDOW_MODAL);
        stageNewRSS.initOwner(stageOwner);
        Scene scene = new Scene(root);
        stageNewRSS.setScene(scene);

        ControllerNewRSS = loader.getController();
        ControllerNewRSS.setMain(this);

        stageNewRSS.showAndWait();
    }

    /*
    Affiche une fenetre modal: RSSManagement.fxml
     */
    public void showWindowRssManagement(Stage stageOwner) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("RSSManagement.fxml"));
        Parent root = loader.load();

        stageRSSManagement = new Stage();
        stageRSSManagement.setTitle("Gestion des flux RSS");
        stageRSSManagement.initModality(Modality.WINDOW_MODAL);
        stageRSSManagement.initOwner(stageOwner);
        Scene scene = new Scene(root);
        stageRSSManagement.setScene(scene);

        ControllerRSSManagement = loader.getController();
        ControllerRSSManagement.setMain(this);
        ControllerRSSManagement.updateListView();

        stageRSSManagement.showAndWait();
    }

    /* Getter Setter */

    public ArrayList<RssReader> getFluxRss() {
        return this.fluxRss;
    }

    public LocalSave getLocalSave() {
        return this.localSave;
    }

    /* Main function */

    public static void main(String[] args) {
        launch(args);
    }
}
