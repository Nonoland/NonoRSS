package fr.nonoland.nonorss.controllers;

import fr.nonoland.nonorss.Main;
import fr.nonoland.nonorss.fx.ArticleTreeItem;
import fr.nonoland.nonorss.utils.Article;
import fr.nonoland.nonorss.utils.LocalSave;
import fr.nonoland.nonorss.utils.RssReader;
import fr.nonoland.nonoutils.logs.Logs;
import javafx.css.PseudoClass;
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

    private TreeItem treeItemRoot;

    @FXML
    private void initialize() {
        /* Ajoute la possibilité de fermer les onglets */
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.ALL_TABS);

        tabWelcomeView.setClosable(true);
        welcomeWebView.getEngine().load("http://nolandartois.fr");

        /* Paramètres de la TreeView */

        treeItemRoot = new TreeItem("FluxRSS");

        articleTreeView.setRoot(treeItemRoot);
        articleTreeView.setShowRoot(false);

        /* TreeView style */


        /* TreeView events */
        articleTreeView.setCellFactory(tv -> new TreeCell<String>(){
            @Override
            public void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                pseudoClassStateChanged(PseudoClass.getPseudoClass("articleSeen"), false);
                setText(item);
                if(getTreeItem() instanceof ArticleTreeItem) {
                    ArticleTreeItem articleTreeItem = (ArticleTreeItem) getTreeItem();
                    if(LocalSave.ifArticleAlreadySeen(articleTreeItem.getArticle())) {
                        setText(item);
                        pseudoClassStateChanged(PseudoClass.getPseudoClass("articleSeen"), true);
                    }
                }
            }
        });

        articleTreeView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                //Ajout d'un onglet lors d'un double clique sur l'arbre des articles
                if(event.getClickCount() == 2) {
                    ArticleTreeItem articleTreeItem = (ArticleTreeItem) articleTreeView.getSelectionModel().getSelectedItem();

                    WebView articleView = new WebView();
                    articleView.getEngine().load(articleTreeItem.getArticle().getLink());

                    /* Réduire le titre de l'article pour l'onglet */
                    String articleTitle = articleTreeItem.getArticle().getName();
                    if(articleTitle.length() > 50) {
                        articleTitle = articleTitle.substring(0, 50) + "...";
                    }

                    Tab newTab = new Tab(articleTitle);
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
        Logs.sendWarning("Mise à jour de la liste des flux...");
        /* Vide le treeItemRoot */
        treeItemRoot.getChildren().clear();

        for(RssReader rss : main.getFluxRss()) {

            rss.readXML();

            TreeItem flux = new TreeItem(rss.getTitle());

            for(Article article : rss.getArticles()) {
                ArticleTreeItem articleItem = new ArticleTreeItem(article);
                flux.getChildren().add(articleItem);
            }

            treeItemRoot.getChildren().add(flux);
        }
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
