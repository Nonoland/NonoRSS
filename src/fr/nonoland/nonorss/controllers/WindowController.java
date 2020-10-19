package fr.nonoland.nonorss.controllers;

import fr.nonoland.nonorss.Main;
import fr.nonoland.nonorss.fx.ArticleTreeItem;
import fr.nonoland.nonorss.utils.Article;
import fr.nonoland.nonorss.utils.RssReader;
import fr.nonoland.nonorss.utils.log.Log;
import fr.nonoland.nonorss.utils.log.StatusCode;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.WebView;

import java.io.IOException;

public class WindowController {
    @FXML
    private TreeView<String> articleTreeView;

    @FXML
    private MenuItem buttonNewRSS;

    @FXML
    private Button updateButton;

    @FXML
    private Tab tabWelcomeView;
    @FXML
    private WebView welcomeWebView;

    @FXML
    private TabPane tabPane;

    @FXML
    private MenuItem rssManagementButton;

    private Main main;
    //private Object MouseEvent;

    public WindowController() {

    }

    @FXML
    private void initialize() {
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

        tabWelcomeView.setClosable(true);
        welcomeWebView.getEngine().load("http://nolandartois.fr");

        articleTreeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(event.getClickCount() == 2) {
                    ArticleTreeItem articleTreeItem = (ArticleTreeItem) articleTreeView.getSelectionModel().getSelectedItem();

                    WebView articleView = new WebView();
                    articleView.getEngine().load(articleTreeItem.getArticle().getLink());

                    Tab newTab = new Tab(articleTreeItem.getArticle().getName());
                    newTab.setClosable(true);

                    newTab.setContent(articleView);

                    tabPane.getSelectionModel().select(newTab);

                    tabPane.getTabs().add(newTab);

                    main.localSave.addURLInHistory(articleTreeItem.getArticle());

                }
            }
        });


    }

    public void setMain(Main main) {
        this.main = main;
    }

    @FXML
    private void handleNewRSS() {
        try {
            main.showNewRSSWindows(main.primaryStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRSSManagement() {
        try {
            main.showRssManagementWindow();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void exitButton() {
        System.exit(0);
    }

    @FXML
    public void updateRSS() {
        Log.sendMessage(StatusCode.Warning, "Mise à jour de la liste des flux...");

        TreeItem rootItem = new TreeItem("FluxRSS");

        for(RssReader rss : main.fluxRss) {
            TreeItem flux = new TreeItem(rss.getTitle());

            for(Article article : rss.getArticles()) {
                ArticleTreeItem articleItem = new ArticleTreeItem(article);

                flux.getChildren().add(articleItem);
            }

            rootItem.getChildren().add(flux);
        }

        articleTreeView.setRoot(rootItem);
        articleTreeView.setShowRoot(false);
    }

}
