package uni.mse.mserss;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by marcel on 19.05.17.
 * Represents a RSS Feed as Collection of RSS Items
 */

public class Channel {
    private String title;
    private String url;
    private String language;
    private Timestamp built;
    private ItemList items;
    private int id;
    private Collection collection;

    public Channel() {
        items = new ItemList();
    }

    public Channel(String URL) {
        setUrl(URL);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Timestamp getBuilt() {
        return built;
    }

    public void setBuilt(Timestamp built) {
        this.built = built;
    }

    public void addItem(Item item) {
        if(item != null) {
            this.items.addItem(item);
        }
    }

    public ItemList getItems() {
        parse();
        return this.items;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setCollection(Collection collection) {
        if(collection != null) {
            this.collection = collection;
        }
    }

    public Collection getCollection() {
        return this.collection;
    }

    public void parseMeta() {
        try {
            Thread tParseMeta = new Thread() {
                @Override
                public void run() {
                    try {
                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        Document doc = dBuilder.parse(getUrl());
                        doc.getDocumentElement().normalize();
                        NodeList nList = doc.getElementsByTagName("channel");

                        for (int temp = 0; temp < nList.getLength(); temp++) {
                            Node nNode = nList.item(temp);
                            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element eElement = (Element) nNode;

                                setTitle(eElement.getElementsByTagName("title").item(0).getTextContent());
                                //TODO parse other items (lang, built, descr, ...)
                            }
                        }
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    catch (SAXException ex) {
                        ex.printStackTrace();
                    }
                    catch (ParserConfigurationException ex) {
                        ex.printStackTrace();
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };

            //setLanguage("DE");
            //setBuilt(new Timestamp(Calendar.getInstance().getTime().getTime()));
            tParseMeta.start();
            tParseMeta.join();  //TODO qnd
        }
        catch (Exception ex) {
            Log.e("channel.parseMeta", ex.toString());
        }
    }

    private void parse() {
        try {
            parseMeta();

            Thread tParse = new Thread() {
                @Override
                public void run() {
                    try {
                        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                        Document doc = dBuilder.parse(getUrl());
                        doc.getDocumentElement().normalize();
                        NodeList nList = doc.getElementsByTagName("item");

                        for (int temp = 0; temp < nList.getLength(); temp++) {
                            Node nNode = nList.item(temp);
                            if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                Element eElement = (Element) nNode;
                                Item i = new Item();
                                i.setTitle(eElement.getElementsByTagName("title").item(0).getTextContent());
                                i.setContent(eElement.getElementsByTagName("description").item(0).getTextContent());
                                i.setUrl(eElement.getElementsByTagName("link").item(0).getTextContent());
                                items.addItem(i);
                            }
                        }
                    }
                    catch (IOException ex) {
                        ex.printStackTrace();
                    }
                    catch (SAXException ex) {
                        ex.printStackTrace();
                    }
                    catch (ParserConfigurationException ex) {
                        ex.printStackTrace();
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            };
            //TODO get title while parsing
            //setLanguage("DE");
            //setBuilt(new Timestamp(Calendar.getInstance().getTime().getTime()));
            tParse.start();
            tParse.join();  //TODO qnd
        }
        catch (Exception ex) {
            Log.e("channel.parse", ex.toString());
        }
    }

}
