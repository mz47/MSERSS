package uni.mse.mserss;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by marcel on 17.05.17.
 * Represents an RSS Feed
 * with Attributes URL, Name and a Timestamp
 */

public class Item {

    private String url;
    private String title;
    private String content;
    private Timestamp refreshed;

    public Item() {
        this.refreshed = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    public Item(String title) {
        this.title = title;
        this.refreshed = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    public Item(String URL, String title) {
        this.url = URL;
        this.title = title;
        this.refreshed = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Timestamp getRefreshed() {
        return refreshed;
    }

    public void setRefreshed(Timestamp refreshed) {
        this.refreshed = refreshed;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
