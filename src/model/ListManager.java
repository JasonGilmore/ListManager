package model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * @author Jason Gilmore
 * 
 * Manages the storage and use of Lists. Part of the model.
 */
public class ListManager {
    private LinkedList<List> linkedList = new LinkedList<List>();
    private ObservableList<List> lists = FXCollections.observableList(linkedList);
    private int size;
    private File txtFile;
    
    /**
     * Constructor function which created the storage file
     * and if it exists, loads the contents from it.
     * 
     * @throws IOException if an input or output exception occurred
     */
    public ListManager() throws IOException {
        txtFile = new File(System.getProperty("user.dir") + "/ListManagerLists.txt");
        txtFile.createNewFile();
        loadContents();
    }
    
    /**
     * Loads the contents from the storage file.
     * The contents are all the lists and their items.
     * 
     * @throws IOException if an input or output exception occurred
     */
    private void loadContents() throws IOException {
        BufferedReader br;
        br = new BufferedReader(new FileReader(txtFile));
        String line;
        while ((line = br.readLine()) != null) {
            if (line.equals("#### NEWLIST")) {
                addList(br.readLine());
            } else {
                lists.get(lists.size()-1).addItem(line);
            }
        }
        br.close();
    }
    
    /**
     * Saves all lists and their items to the storage file.
     * 
     * @throws IOException if an input or output exception occurred
     */
    public void saveAll() throws IOException {
        BufferedWriter bw;
        bw = new BufferedWriter(new FileWriter(txtFile));
        for (List list : lists) {
            bw.write("#### NEWLIST");
            bw.newLine();
            bw.write(list.getName());
            bw.newLine();
            for (int i = 0; i < list.getSize(); i++) {
                bw.write(list.getItem(i));
                bw.newLine();
            }
        }
        bw.close();
    }
    
    /**
     * Checks whether a list exists.
     * 
     * @param name the list name to check
     * @return     true if list exists, or false if it does not
     */
    public boolean isList(String name) {
        for (List list : lists) {
            if (list.getName().toLowerCase().equals(name)) return true;
        }
        return false;
    }
    
    /**
     * Returns all the lists.
     * 
     * @return all lists
     */
    public ObservableList<List> getLists() {
        return lists;
    }
    
    /**
     * Returns the specified list based on its index.
     * 
     * @param index the index of the list
     * @return      the specified list
     */
    public List getList(int index) {
        return lists.get(index);
    }
    
    /**
     * Adds a new list.
     * 
     * @param name the name of the new list to add
     */
    public void addList(String name) {
        lists.add(new List(name));
        size += 1;
    }
    
    /**
     * Removes a list.
     * 
     * @param index the index of the list to be removed
     */
    public void removeList(int index) {
        lists.remove(index);
        size -= 1;
    }
    
    /**
     * Returns the number of lists.
     * 
     * @return the number of lists
     */
    public int getSize() {
        return size;
    }
}
