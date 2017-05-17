package uni.mse.mserss;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.net.URL;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by marcel on 17.05.17.
 * Represents an RSS Feed
 * with Attributes URL, Name and a Timestamp
 */

public class Item {

    private String url;
    private String name;
    private String headline;
    private String content;
    private Timestamp refreshed;
    private XMLReader xmlReader;

    public Item(String URL) {
        this.url = URL;
        this.name = null;
        this.refreshed = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    public Item(String URL, String Name) {
        this.url = URL;
        this.name = Name;
        this.refreshed = new Timestamp(Calendar.getInstance().getTime().getTime());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getRefreshed() {
        return refreshed;
    }

    public void setRefreshed(Timestamp refreshed) {
        this.refreshed = refreshed;
    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(String headline) {
        this.headline = headline;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void Parse() {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new URL(url).openStream());
            Element root = doc.getDocumentElement();
            NodeList nodeList = doc.getElementsByTagName("item");
            Element e = (Element) nodeList.item(0);
            Log.i("item.parse.headline", e.getAttribute("title"));
        }
        catch (Exception ex) {
            Log.e("item.parse", ex.getMessage());
        }
    }

    private void GetHeadline() {

    }

    private void GetContent() {

    }

}
