package fr.nonoland.nonorss.utils;

import fr.nonoland.nonorss.Main;
import fr.nonoland.nonoutils.logs.Logs;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class LocalSave {

    private Path pathProperties;
    private Path pathHistoryFile;

    private Properties properties;

    private ArrayList<String> rss;

    private Main main;
    public LocalSave(Main main) throws IOException, ParserConfigurationException {
        this.main = main;

        this.pathProperties = Paths.get(System.getProperty("user.home"), ".nonorss", "nonorss.properties");

        this.pathHistoryFile = Paths.get(System.getProperty("user.home"), ".nonorss", "history", "test.hist");

        //Vérifie si le dossier a déjà créer
        if(!Files.exists(pathProperties.getParent())) {
            Logs.sendWarning("Le dossier n'exite pas ! Création du dossier !");
            Files.createDirectory(pathProperties.getParent());
            if(!Files.exists(pathProperties))
                Files.createFile(pathProperties);
        }

        //Vérifie si le dossier "history" existe
        if(!Files.exists(pathHistoryFile.getParent())) {
            Logs.sendWarning("Le dossier n'existe pas ! Création du dossier !");
            Files.createDirectory(pathHistoryFile.getParent());
            if(!Files.exists(pathHistoryFile))
                Files.createFile(pathHistoryFile);
        }

        //Chargement du fichier properties
        properties = new Properties();
        try(InputStream stream = Files.newInputStream(pathProperties)) {
            properties.load(stream);
        }

        //Chargement des flux stockés dans le fichier properties
        rss = new ArrayList<String>(Arrays.asList(properties.get("fluxrss").toString().split("\\|")));

        if(rss.size() == 1 && rss.get(0).equals(""))
            rss = new ArrayList<String>();

        for(String s : rss) {
            //RssReader rss = RssReader.getRssReaderWithURL(s);
            RssReader rss = new RssReader(s);
            rss.setMain(main);
            rss.readXML();
            main.getFluxRss().add(rss);
        }

    }

    public void addRSS(String url) {
        rss.add(url);
        try {
            saveProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void saveProperties() throws IOException {
        properties.setProperty("fluxrss", String.join("|", rss));
        properties.store(Files.newOutputStream(pathProperties), null);
    }

    public void addURLInHistory(Article article) {

        /* Create File */
        Path path = Paths.get(System.getProperty("user.home"), ".nonorss", "history", hash(article.getLink()) + ".hist");

        try {
            Files.createFile(path);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public boolean ifArticleAlreadySeen(Article article) {
        Path path = Paths.get(System.getProperty("user.home"), ".nonorss", "history", hash(article.getLink()) + ".hist");

        return Files.exists(path);
    }

    private String hash(String m) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] messageDigest = md.digest(m.getBytes());
        BigInteger no = new BigInteger(1, messageDigest);
        String hashtext = no.toString(16);
        while(hashtext.length() < 32) {
            hashtext = "0" + hashtext;
        }

        return hashtext;
    }
}
