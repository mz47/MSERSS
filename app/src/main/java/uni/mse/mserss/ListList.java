package uni.mse.mserss;

import java.util.ArrayList;

/**
 * Created by marcel on 02.06.17.
 */

public class ListList {
    private ArrayList<List> lists;

    public ListList() {
        lists = new ArrayList<>();
    }

    public void add(List list) {
        if(list != null) {
            lists.add(list);
        }
    }

    public List get(int position) {
        if(lists != null && position >= 0) {
            return lists.get(position);
        }
        return null;
    }

    public ArrayList<List> getLists() {
        return lists;
    }

    public ArrayList<String> getNames() {
        if(lists != null) {
            ArrayList<String> names = new ArrayList<>();
            for(List l : lists) {
                names.add("(list) " + l.getName());
            }
            return names;
        }
        return null;
    }
}
