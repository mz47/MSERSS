package uni.mse.mserss;
import java.util.ArrayList;
/**
 * Created by marcel on 17.05.17.
 * Represents a Collection of ItemsActivity (RSS Feeds)
 */

public class List {

    private ArrayList<Item> items;
    private String name;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public List() {
        items = new ArrayList<Item>();
    }

    public void Add(Item item) {
        if(item != null) {
            items.add(item);
        }
    }

    public Item Get(int index) {
        if(index >= 0 && items.get(index) != null) {
            return items.get(index);
        }
        else {
            return null;
        }
    }

    public void Remove(Item item) {
        if(item != null && items.contains(item)) {
            items.remove(item);
        }
    }

    public void Remove(int index) {
        if(index > 0 && items.get(index) != null) {
            items.remove(index);
        }
    }

    public ArrayList ToUrlList() {
        if(items != null && items.size() > 0) {
            ArrayList<String> urlList = new ArrayList<>();
            for(Item i : items) {
                urlList.add(i.getUrl());
            }
            return urlList;
        }
        return null;
    }

    public ArrayList ToNameList() {
        if(items != null && items.size() > 0) {
            ArrayList<String> nameList = new ArrayList<>();
            for(Item i : items) {
                nameList.add(i.getTitle() + ":" + i.getUrl());
            }
            return nameList;
        }
        return null;
    }
}
