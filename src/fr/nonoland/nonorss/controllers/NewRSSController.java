package fr.nonoland.nonorss.controllers;

import fr.nonoland.nonorss.Main;
import fr.nonoland.nonorss.utils.RssReader;
import fr.nonoland.nonorss.utils.log.Log;
import fr.nonoland.nonorss.utils.log.StatusCode;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class NewRSSController {

    @FXML
    private VBox mainVbox;
    @FXML
    private Text labelUrl;
    @FXML
    private TextField textFieldUrl;
    @FXML
    private Button buttonSave;

    private Main main;

    @FXML
    private void initialize() {

    }

    @FXML
    private void handleButtonSave() throws IOException, SAXException, ParserConfigurationException {
        Log.sendMessage(StatusCode.Info, "Enregistrement d'un nouveau flux RSS: " + textFieldUrl.getText());

        /* Création de l'objet RssReader */
        //RssReader newRss = RssReader.getRssReaderWithURL(new URL(textFieldUrl.getText()));
        RssReader newRss = new RssReader((textFieldUrl.getText()));
        newRss.readXML();
        //Ajout dans la liste des flux RSS
        main.getFluxRss().add(newRss);
        //Ajout dans les fichiers properties
        main.getLocalSave().addRSS(textFieldUrl.getText());

        if(newRss != null)
            main.stageNewRSS.close();
    }

    public void setMain(Main main) {
        this.main = main;
    }
}
