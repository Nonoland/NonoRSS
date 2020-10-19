package fr.nonoland.nonorss.controllers;

import fr.nonoland.nonorss.Main;
import fr.nonoland.nonorss.utils.RssReader;
import fr.nonoland.nonorss.utils.log.Log;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class RSSManagementController {

    @FXML
    private Button modifyButton;

    @FXML
    private ListView rssList;

    private Main main;

    @FXML
    private void initialize() {
        rssList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        rssList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2) {
                    try {
                        showRssEdit(rssList.getSelectionModel().getSelectedIndex());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void setMain(Main main) {
        this.main = main;
    }

    public void updateListView() {
        rssList.getItems().clear();
        for(RssReader rss : main.getFluxRss()) {
            rssList.getItems().add(rss);
        }
    }

    public void handleModify() {
        try {
            showRssEdit(rssList.getSelectionModel().getSelectedIndex());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void handleAdd() {
        try {
            main.showNewRSSWindows(main.stageRSSManagement);
        } catch (IOException e) {
            e.printStackTrace();
        }

        updateListView();
    }

    public void showRssEdit(int id) throws IOException {
        RSSManagementEditController rssManagementEditController;

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../RSSManagementEdit.fxml"));
        Parent root = loader.load();

        Stage stageRSSManagementEdit = new Stage();
        stageRSSManagementEdit.setTitle("Modification de flux RSS");
        stageRSSManagementEdit.initModality(Modality.WINDOW_MODAL);
        stageRSSManagementEdit.initOwner(main.stageRSSManagement);
        Scene scene = new Scene(root);
        stageRSSManagementEdit.setScene(scene);

        rssManagementEditController = loader.getController();
        rssManagementEditController.setRssReader(main, id);

        stageRSSManagementEdit.showAndWait();
    }

}
