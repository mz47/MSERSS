package uni.mse.mserss;

import android.os.AsyncTask;
import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by marcel on 17.05.17.
 * Represents an async Task to parse RSS Feed
 */

public class ReceiveFeedTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse("https://rss.golem.de/rss.php?feed=RSS2.0");
            doc.getDocumentElement().normalize();
            Log.d("Item.parse", "Root element :" + doc.getDocumentElement().getNodeName());

            NodeList nList = doc.getElementsByTagName("item");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);

                System.out.println("\nCurrent Element :" + nNode.getNodeName());

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    System.out.println("title: " + eElement.getElementsByTagName("title").item(0).getTextContent());

                }
            }


            return "";
        } catch (Exception ex) {
            Log.e("ReceiveFeedTask", ex.toString());
            return null;
        }
    }

    @Override
    protected void onPostExecute(String message) {
        //process message
    }

}
