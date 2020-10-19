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

    @FXML
    private ProgressBar progressBarUpateRss;

    private Main main;

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

                    main.getLocalSave().addURLInHistory(articleTreeItem.getArticle());

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
            main.showWindowNewRSS(main.primaryStage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRSSManagement() {
        try {
            main.showWindowRssManagement(main.primaryStage);
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

        for(RssReader rss : main.getFluxRss()) {

            rss.readXML();

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

    public void updateProgressBar() {
        int rssReaderReady = 0;
        for(RssReader rss : main.getFluxRss()) {
            if(rss.isDownloadGood())
                rssReaderReady++;
        }

        progressBarUpateRss.setProgress(rssReaderReady / main.getFluxRss().size());

        if(progressBarUpateRss.getProgress() == 1)
            updateRSS();
    }

}
