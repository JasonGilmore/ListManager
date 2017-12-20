package model;

import java.util.LinkedList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Jason Gilmore
 * 
 * Used to group together similar items. Part of the model.
 * This class contains a name, a size and all the items stored.
 */
public class List {
    private LinkedList<String> linkedList = new LinkedList<String>();
    private ObservableList<String> items = FXCollections.observableList(linkedList);
    private String name;
    private int size;
    
    /**
     * Constructor function that sets the name of the list
     * and the size to 0.
     * 
     * @param name the name of the list
     */
    public List(String name) {
        this.name = name;
        size = 0;
    }
    
    /**
     * Constructor function that sets the name of the list
     * and adds elements from another list.
     * 
     * @param name the name of the list
     * @param items the items from another list
     */
    public List(String name, ObservableList<String> items) {
        this.name = name;
        for (String item : items) {
            this.addItem(item);
        }
    }
    
    /**
     * toString function.
     * 
     * @return name of the list
     */
    @Override
    public String toString() {
        return name;
    }
    
    /**
     * Returns the name of the list.
     * 
     * @return name of the list
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the new name.
     * 
     * @param name new name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Returns the specified item.
     * 
     * @param index index of item
     * @return the item
     */
    public String getItem(int index) {
        return items.get(index);
    }
    
    /**
     * Returns all items.
     * 
     * @return all items
     */
    public ObservableList<String> getItems() {
        return items;
    }
    
    /**
     * Adds an item.
     * 
     * @param item the item to add
     */
    public void addItem(String item) {
        items.add(item);
        size++;
    }
    
    /**
     * Removes an item at a specified index.
     * 
     * @param index the index of the item to remove
     */
    public void removeItem(int index) {
        items.remove(index);
        size--;
    }
    
    /**
     * Checks whether the list is empty.
     * 
     * @return true if list is empty, false if not.
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }
    
    /**
     * Returns the list size.
     * 
     * @return list size
     */
    public int getSize() {
        return size;
    }
}
