package classes;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Cyrus on 15 Oct 2017.
 */

public class Category implements Serializable {

    private ArrayList<Item> items;
    private ArrayList<Item> answeredItems;

    public Category() {
        items = new ArrayList<>();
        answeredItems = new ArrayList<>();
    }

    public Category(ArrayList<Item> items) {
        this.items = items;
        answeredItems = new ArrayList<>();
    }

    public void add(Item item) {
        items.add(item);
    }

    public boolean canGet() {
        if(!items.isEmpty())
            return true;
        return false;
    }

    /*
    This function returns a "random" Item from the list
    and adds that to the answered list.
     */
    public Item getItem() {
        Item temp = items.remove(0);
        answeredItems.add(temp);
        return temp;
    }

}
