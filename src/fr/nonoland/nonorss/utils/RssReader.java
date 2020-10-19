package fr.nonoland.nonorss.utils;

import fr.nonoland.nonorss.Main;
import fr.nonoland.nonorss.utils.log.Log;
import fr.nonoland.nonorss.utils.log.StatusCode;
import javafx.application.Platform;
import javafx.scene.image.Image;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;

public class RssReader {

    private final String urlRssReader;

    /* XML File */
    private String xmlFile;
    private DocumentBuilder dBuilder;
    private Document document;

    /* Website information */
    /* required */
    private String title;
    private String link;
    private String description;
    /* optional */
    private String language;
    private String copyright;
    private String managingEditor;
    private String webMaster;
    private Date pubDate;
    private Date lastBuildDate;
    private String category;
    private String generator;
    private String docs;
    private String cloud;
    private int ttl;
    private Image image;
    private String textInput;
    private int skipHours;
    private int skipDays;

    private ArrayList<Article> articles = new ArrayList<Article>();

    /* Thread */
    private Thread threadDownloadRss;
    private boolean downloadGood = false;

    /* Main */
    private Main main;

    public RssReader(String urlRssReader) throws IOException, ParserConfigurationException {
        this.urlRssReader = urlRssReader;

        threadDownloadRss = new Thread(new ThreadDownloadRss());
        threadDownloadRss.setDaemon(true);
        threadDownloadRss.start();

    }

    public void readXML() {
        if(downloadGood) {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            try {
                this.dBuilder = dbFactory.newDocumentBuilder();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

            try {
                this.document = dBuilder.parse(new ByteArrayInputStream(xmlFile.getBytes()));
            } catch (SAXException e) {
                Log.sendMessage(StatusCode.Error, "Le fichier n'est pas un Flux RSS !");
                e.printStackTrace();
            } catch (IOException e) {
                Log.sendMessage(StatusCode.Error, "Erreur pendant la lecture du ficher !");
                e.printStackTrace();
            }
            Log.sendMessage(StatusCode.Warning, "Xml Version: " + document.getXmlVersion());
            this.document.getDocumentElement().normalize();

            //Load all information in channel
            getChannelInformation();

            Log.sendMessage(StatusCode.Info, "Chargement des articles du site: " + title + " | " + link);

            /* Item list */
            NodeList itemList = document.getElementsByTagName("item");
            for (int i = 0; i < itemList.getLength(); i++) {
                Node articleNode = itemList.item(i);
                if (articleNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) articleNode;
                    String articleTitle = e.getElementsByTagName("title").item(0).getTextContent();
                    String articleLink = e.getElementsByTagName("link").item(0).getTextContent();
                    String articleDescription = e.getElementsByTagName("description").item(0).getTextContent();
                    this.articles.add(new Article(articleTitle, articleLink, articleDescription));
                    Log.sendMessage(StatusCode.Info, articleTitle);
                }
            }

            Log.sendMessage(StatusCode.Info, "Nombres d'articles trouvés: " + articles.size());
        }
    }

    private void getChannelInformation() {
        if(document.getElementsByTagName("channel").getLength() != 0) {
            /* Channel */
            Element channel = (Element) document.getElementsByTagName("channel").item(0);

            /* Save required information */
            /* Regarde si les informations obligatoires se trouvent dans le flux RSS */
            if(channel.getElementsByTagName("title").getLength() + channel.getElementsByTagName("link").getLength() + channel.getElementsByTagName("description").getLength() >= 3) {
                this.title = channel.getElementsByTagName("title").item(0).getTextContent();
                this.link = channel.getElementsByTagName("link").item(0).getTextContent();
                this.description = channel.getElementsByTagName("description").item(0).getTextContent();
            } else {
                Log.sendMessage(StatusCode.Error, "Les informations obligatoires ne sont pas complètes !");
                return;
            }

            /* Save optional information */
            if(channel.getElementsByTagName("language").getLength() != 0)
                this.language = channel.getElementsByTagName("language").item(0).getTextContent();
            else
                Log.sendMessage(StatusCode.Warning, "La langue n'est pas disponible.");

            if(channel.getElementsByTagName("copyright").getLength() != 0)
                this.copyright = channel.getElementsByTagName("copyright").item(0).getTextContent();
            else
                Log.sendMessage(StatusCode.Warning, "Le copyright n'est pas disponible.");

            if(channel.getElementsByTagName("managingEditor").getLength() != 0)
                this.managingEditor = channel.getElementsByTagName("managingEditor").item(0).getTextContent();
            else
                Log.sendMessage(StatusCode.Warning, "L'email managingEditor n'est pas disponible.");

            if(channel.getElementsByTagName("webMaster").getLength() != 0)
                this.webMaster = channel.getElementsByTagName("webMaster").item(0).getTextContent();
            else
                Log.sendMessage(StatusCode.Warning, "L'email du webMaster n'est pas disponible.");

            //TODO Add var with Date later
            if(channel.getElementsByTagName("category").getLength() != 0)
                this.category = channel.getElementsByTagName("category").item(0).getTextContent();
            else
                Log.sendMessage(StatusCode.Warning, "La categorie n'est pas disponible.");

            if(channel.getElementsByTagName("generator").getLength() != 0)
                this.generator = channel.getElementsByTagName("generator").item(0).getTextContent();
            else
                Log.sendMessage(StatusCode.Warning, "Le générateur n'est pas disponible.");

            if(channel.getElementsByTagName("docs").getLength() != 0)
                this.docs = channel.getElementsByTagName("docs").item(0).getTextContent();
            else
                Log.sendMessage(StatusCode.Warning, "Le docs n'est pas disponible.");

            if(channel.getElementsByTagName("cloud").getLength() != 0)
                this.cloud = channel.getElementsByTagName("cloud").item(0).getTextContent();
            else
                Log.sendMessage(StatusCode.Warning, "Le cloud n'est pas disponible.");

            if(channel.getElementsByTagName("ttl").getLength() != 0)
                this.ttl = Integer.parseInt(channel.getElementsByTagName("ttl").item(0).getTextContent());
            else
                Log.sendMessage(StatusCode.Warning, "Le ttl n'est pas disponible.");

            //TODO Add image later

            if(channel.getElementsByTagName("textInput").getLength() != 0)
                this.textInput = channel.getElementsByTagName("textInput").item(0).getTextContent();
            else
                Log.sendMessage(StatusCode.Warning, "Le textInput n'est pas disponible.");

            //TODO Add skipHours and Days later

        } else {
            Log.sendMessage(StatusCode.Error, "Aucun élément <channel> dans le flux RSS.");
            return;
        }
    }

    public Node getRssNode() {
        return document.getDocumentElement();
    }

    public String getTitle() {
        return this.title;
    }

    public String getDescription() {
        return this.description;
    }

    public String getLink() {
        return this.link;
    }

    public String getLanguage() {
        return this.language;
    }

    public ArrayList<Article> getArticles() {
        return this.articles;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String toString() {
        return this.title;
    }

    public boolean isDownloadGood() {
        return this.downloadGood;
    }

    public void setMain(Main main) {
        this.main = main;
    }

    private class ThreadDownloadRss implements Runnable {

        @Override
        public void run() {

                    Log.sendMessage("Lancement du téléchargement ! " + urlRssReader);

                    URL url = null;
                    try {
                        url = new URL(urlRssReader);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                    URLConnection openConnection = null;
                    try {
                        openConnection = url.openConnection();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    /*
                    Ajout de l'user-agent pour éviter l'erreur 403
                    */
                    openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");

                    InputStream inputStream = null;
                    try {
                        inputStream = openConnection.getInputStream();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    StringBuilder textBuilder = new StringBuilder();
                    try (Reader reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName(StandardCharsets.UTF_8.name())))) {
                        int c = 0;
                        while ((c = reader.read()) != -1) {
                            textBuilder.append((char) c);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    xmlFile = textBuilder.toString();
                    downloadGood = true;

                    if(main != null) {
                        Runnable updater = new Runnable() {
                            @Override
                            public void run() {
                                main.ControllerWindow.updateProgressBar();
                            }
                        };

                        Platform.runLater(updater);

                    }

        }

    }

}
