package uni.mse.mserss;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
    private ItemList items;
    private int id;
    private String type;
    private int refresh;
    private String signature;

    public static final String TYPE_RSS = "RSS";
    public static final String TYPE_NONRSS = "NONRSS";

    public Channel() {
        items = new ItemList();
    }

    public Channel(String URL) {
        setUrl(URL);
    }

    public String getSignature() {
        return signature;
    }

    public int getRefresh() {
        return refresh;
    }

    public void setRefresh(int refresh) {
        this.refresh = refresh;
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

    public void addItem(Item item) {
        if(item != null) {
            this.items.addItem(item);
        }
    }

    public ItemList getItems() {
        return this.items;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
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


                        if (nList != null && nList.getLength() > 0) {    // Parse RSS Feed
                            for (int i = 0; i < nList.getLength(); i++) {
                                Node nNode = nList.item(i);
                                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                                    Element eElement = (Element) nNode;

                                    title = eElement.getElementsByTagName("title").item(0).getTextContent();
                                    type = Channel.TYPE_RSS;
                                }
                            }
                        }
                        else {  // No RSS Feed available
                            type = Channel.TYPE_NONRSS;
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
            tParseMeta.start();
            tParseMeta.join();
        }
        catch (Exception ex) {
            Log.e("parseMeta", ex.toString());
        }
    }

    public void parse() {
        try {
            final int channelId = this.id;
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
                                i.setChannelId(channelId);
                                items.addItem(i);
                            }
                        }
                        signature = items.getItem(0).getTitle().hashCode() + "";
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
            tParse.start();
            tParse.join();  //TODO qnd
        }
        catch (Exception ex) {
            Log.e("parse", ex.toString());
        }
    }

    public Item getLastItem() {
        if(items != null && items.getSize() > -1) {
            return items.getItem(items.getSize() - 1);
        }
        return null;
    }
}
