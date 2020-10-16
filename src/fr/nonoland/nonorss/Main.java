package fr.nonoland.nonorss;

import fr.nonoland.nonorss.utils.RssReader;
import fr.nonoland.nonorss.utils.log.Log;
import fr.nonoland.nonorss.utils.log.StatusCode;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;

public class Main extends Application {

    public RssReader rssReader;

    private WindowController windowController;

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("Window.fxml"));
        //Parent root = FXMLLoader.load(getClass().getResource("Window.fxml"));
        Parent root = loader.load();
        primaryStage.setTitle("NonoRSS");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();

        URL feedSource = new URL("https://korben.info/feed");
        this.rssReader = RssReader.getRssReaderWithURL(feedSource);

        windowController = loader.getController();
        windowController.setMain(this);
    }

    public RssReader getRssReader() {
        return this.rssReader;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
