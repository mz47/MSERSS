package uni.mse.mserss;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by marcel on 22.05.17.
 */

public class GetRssTask extends AsyncTask<String, String, String> {
    private Context context;
    private ItemList items;
    private ProgressDialog dialog;

    public GetRssTask()
    {
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        items = new ItemList();
    }

    @Override
    protected String doInBackground(String... urls) {
        String url = urls[0];
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse("https://rss.golem.de/rss.php?feed=RSS2.0");
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("item");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    items.addItem(new Item(eElement.getElementsByTagName("title").item(0).getTextContent()));
                    //Log.d("channel.parse", "title: " + eElement.getElementsByTagName("title").item(0).getTextContent());
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
        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

    }

    @Override
    protected void onProgressUpdate(String... values) {
        super.onProgressUpdate(values);
    }

    public ItemList getItemList() {
        Log.d("getrsstask.getitemlist", "size: " + items.getItems().size());
        return this.items;
    }

}
