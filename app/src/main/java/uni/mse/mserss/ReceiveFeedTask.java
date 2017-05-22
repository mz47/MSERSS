/*package uni.mse.mserss;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/*
 * Created by marcel on 17.05.17.
 * Represents an async Task to parse RSS Feed
 */

/*public class ReceiveFeedTask extends AsyncTask<String, Void, Void> {

    private ArrayList<String> items;
    private Activity activity;

    public ReceiveFeedTask(Activity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }
    @Override
    protected void doInBackground(String... params) {
        try {
            ArrayList<String> items = new ArrayList<>();
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse("https://rss.golem.de/rss.php?feed=RSS2.0");
            doc.getDocumentElement().normalize();
            NodeList nList = doc.getElementsByTagName("item");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    items.add(eElement.getElementsByTagName("title").item(0).getTextContent());
                }
            }

        } catch (Exception ex) {
            Log.e("ReceiveFeedTask", ex.toString());
        }
    }
    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
*/