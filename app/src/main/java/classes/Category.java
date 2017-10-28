package classes;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 * Created by Cyrus on 15 Oct 2017.
 */

public class Category implements Serializable {

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    private int imageID; //This is the image representing the category that will be shown in the ChooseCategory screen
    private String name;
    private ArrayList<Item> items;
    private ArrayList<Item> answeredItems;

    private Random r; //Generate random position of the next item to get
    private Item previousItem; //Keep track of the previous Item returned



    /**
     * This function creates a new Category class with empty variables.
     */
    public Category() {
        items = new ArrayList<>();
        answeredItems = new ArrayList<>();

    }

    /**
     * This function creates a new Category class with empty variables.
     */
    public Category(String name, int imageID) {
        items = new ArrayList<>();
        answeredItems = new ArrayList<>();
        this.name = name;
        this.imageID = imageID;
        r = new Random();
        previousItem = null;
    }

    /**
     * This function creates a Category class from an existing Item ArrayList.
     * @param items
     */
    public Category(ArrayList<Item> items) {
        this.items = items;
        answeredItems = new ArrayList<>();
    }

    /**
     * This function adds an Item to the Category
     * @param item
     */
    public void add(Item item) {
        items.add(item);
    }

    /**
     * This function shuffles the Items in the Category.
     */
    public void shuffleItems() {
        Collections.shuffle(items);
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean canGet() {
        if(!items.isEmpty())
            return true;
        return false;
    }

    public String getName() {
        return name;
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

}
