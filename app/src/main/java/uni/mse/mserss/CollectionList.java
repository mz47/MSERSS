package uni.mse.mserss;

import java.util.ArrayList;

/**
 * Created by marcel on 02.06.17.
 */

public class CollectionList {
    private ArrayList<Collection> collections;

    public CollectionList() {
        collections = new ArrayList<>();
    }

    public void add(Collection collection) {
        if(collection != null) {
            collections.add(collection);
        }
    }

    public Collection get(int position) {
        if(collections != null && position >= 0) {
            return collections.get(position);
        }
        return null;
    }

    public ArrayList<Collection> getCollections() {
        return collections;
    }

    public ArrayList<String> getNames() {
        if(collections != null) {
            ArrayList<String> names = new ArrayList<>();
            for(Collection l : collections) {
                names.add(l.getName());
            }
            return names;
        }
        return null;
    }

    public int getSize() {
        if(collections != null) {
            return collections.size();
        }
        return -1;
    }
}
