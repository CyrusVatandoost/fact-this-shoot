package classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Cyrus on 15 Oct 2017.
 */

public class Category implements Serializable {

    private ArrayList<Item> items;
    private ArrayList<Item> answeredItems;

    /**
     * This function creates a new Category class with empty variables.
     */
    public Category() {
        items = new ArrayList<>();
        answeredItems = new ArrayList<>();
    }

    /**
     * This function creates a Category class from an existing Item ArrayList.
     * @param items
     */
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

    /**
    This function returns a "random" Item from the list
    and adds that to the answered list.
     */
    public Item getItem() {
        Item temp = items.remove(0);
        answeredItems.add(temp);
        return temp;
    }

    public void shuffleItems() {
        Collections.shuffle(items);
    }

}
