package fr.nonoland.nonorss.fx;

import fr.nonoland.nonorss.utils.Article;
import javafx.scene.control.TreeItem;

public class ArticleTreeItem extends TreeItem<String> {

    private Article article;

    public ArticleTreeItem(Article article) {
        super(article.getName());
        this.article = article;
    }

    public Article getArticle() {
        return article;
    }

    public String toString() {
        return article.getName();
    }

}
