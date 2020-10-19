package fr.nonoland.nonorss.controllers;

import fr.nonoland.nonorss.Main;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RSSManagementEditController {

    @FXML
    private TextField urlTextField;

    @FXML
    private Button modifyButton;

    private int idRss;

    private Main main;

    @FXML
    public void initialize() {

    }

    public void setRssReader(Main main, int id) {
        this.main = main;
        this.idRss = id;

        urlTextField.setText(main.getFluxRss().get(id).getLink());
    }

    public void handleModify() {
        main.getFluxRss().get(idRss).setLink(urlTextField.getText());
        ((Stage)urlTextField.getScene().getWindow()).close();
    }
}
