package uni.mse.mserss;

import android.util.Log;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by marcel on 19.05.17.
 * Represents a RSS Feed as Collection of RSS Items
 */

public class Channel {
    private String title;
    private String url;
    private String language;
    private Timestamp built;
    private ArrayList<Item> items;

    public Channel() {
        items = new ArrayList<>();
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
            this.items.add(item);
        }
    }

    public ArrayList<String> getItems() {
        if(items != null) {
            ArrayList<String> i = new ArrayList<>();
            for(Item item : items) {
                i.add(item.getHeadline());
            }
            return i;
        }
        return null;
    }

    public void parse() {
        try {
            //ReceiveFeedTask receiver = new ReceiveFeedTask();
            //receiver.execute(url);

            setTitle("Test Channel 1");
            setLanguage("DE");
            setBuilt(new Timestamp(Calendar.getInstance().getTime().getTime()));
            addItem(new Item("abc", "name0"));
            addItem(new Item("def", "name1"));
            addItem(new Item("ghi", "name2"));
        }
        catch (Exception ex) {
            Log.e("channel.parse", ex.toString());
        }
    }
}
