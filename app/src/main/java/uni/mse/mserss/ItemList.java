package uni.mse.mserss;

import java.util.ArrayList;

/**
 * Created by marcel on 22.05.17.
 */

public class ItemList {
    private ArrayList<Item> items;

    public ItemList() {
        items = new ArrayList<>();
    }

    public void addItem(Item item) {
        if(item != null) {
            items.add(item);
        }
    }

    public Item getItem(int position) {
        if(items != null && position >= 0) {
            return items.get(position);
        }
        return null;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public ArrayList<String> getTitles() {
        if(items != null) {
            ArrayList<String> titles = new ArrayList<>();
            for(Item i : items) {
                titles.add(i.getTitle());
            }
            return titles;
        }
        return null;
    }
}
