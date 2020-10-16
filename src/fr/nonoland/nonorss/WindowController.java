package fr.nonoland.nonorss;

import fr.nonoland.nonorss.utils.RssReader;
import fr.nonoland.nonorss.utils.log.Log;
import fr.nonoland.nonorss.utils.log.StatusCode;
import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import sun.reflect.generics.tree.Tree;

public class WindowController {
    @FXML
    private TreeView<String> articleTreeView;

    private Main main;
    private RssReader rssReader;

    public WindowController() {

    }

    @FXML
    private void initialize() {

    }

    public void setMain(Main main) {
        this.main = main;
        this.rssReader = main.rssReader;
    }

    //Update TreeView with articles
    public void update() {
        TreeItem<String> rootItem = new TreeItem<String>(rssReader.getTitle());

        for(RssReader.Article article : rssReader.getArticles()) {
            TreeItem<String> articleItem = new TreeItem<String>(article.getName());
            rootItem.getChildren().add(articleItem);
        }

        rootItem.setExpanded(true);

        articleTreeView.setRoot(rootItem);

        Log.sendMessage(StatusCode.Info, rssReader.getArticles().size()+"");
    }
}
