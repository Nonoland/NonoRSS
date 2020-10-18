package fr.nonoland.nonorss.utils;

import fr.nonoland.nonorss.Main;
import fr.nonoland.nonorss.utils.log.Log;
import fr.nonoland.nonorss.utils.log.StatusCode;

import javax.print.DocFlavor;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class LocalSave {

    private String separator;
    private Path pathProperties;

    private Properties properties;

    private ArrayList<String> rss;

    private Main main;
    public LocalSave(Main main) throws IOException {
        this.main = main;

        this.separator = System.getProperty("file.separator");
        this.pathProperties = Paths.get(System.getProperty("user.home"), ".nonorss", "nonorss.properties");

        //Vérifie si le dossier a déjà créer
        if(!Files.exists(pathProperties.getParent())) {
            Log.sendMessage(StatusCode.Warning, "Le dossier n'exite pas ! Création du dossier !");
            Files.createDirectory(pathProperties.getParent());
            if(!Files.exists(pathProperties))
                Files.createFile(pathProperties);
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
            RssReader rss = RssReader.getRssReaderWithURL(s);
            main.fluxRss.add(rss);
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
}
