package controller;


import java.io.IOException;
import model.ListManager;
import model.List;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldListCell;
import javafx.scene.input.*;
import javafx.scene.text.*;
import javafx.util.StringConverter;

/**
 * @author Jason Gilmore
 * 
 * The controller object which displays the model on the
 * view and handles all user interaction.
 */
public class ListController {
    @FXML ListView listsLw;
    @FXML TextField newListTf;
    @FXML Button removeListBtn;
    @FXML Text dupErrorTxt;
    @FXML ListView itemsLw;
    @FXML Label itemsPlaceholder;
    @FXML Button addItemBtn;
    @FXML TextField newItemTf;
    @FXML Button removeItemBtn;
    @FXML Button saveBtn;
    ListManager listManager;
    
    /**
     * Constructor function which initialises the listManager.
     */
    public ListController() {
        try {
            listManager = new ListManager();
        } catch (IOException ex) {
            exceptionAlert(ex.getMessage());
        }
    }
    
    /**
     * Sets data on the view for the user to use and interact with.
     */
    @FXML private void initialize() {
        listsLw.setItems(listManager.getLists());
        listsLw.setEditable(true);
        listsLw.setCellFactory(TextFieldListCell.forListView(new StringConverter<List>() {
            @Override
            public String toString(List list) {
                return list.toString();
            }

            @Override
            public List fromString(String string) {
                if (!listManager.isList(string.toLowerCase())) {
                    enableSave();
                    return new List(string, getSelectedList().getItems());
                }
                else {
                    startErrorTxt();
                    return getSelectedList();
                }
            }
        }));
        listsLw.getSelectionModel().select(0);
        handleSelectList();
        itemsLw.setEditable(true);
        itemsLw.setCellFactory(TextFieldListCell.forListView());
        itemsLw.getSelectionModel().selectedItemProperty().addListener(
                (o, oldItem, newItem) -> removeItemBtn.setDisable(newItem == null));
        itemsLw.setOnEditCommit(new EventHandler<ListView.EditEvent<String>>() {
            @Override
            public void handle(ListView.EditEvent<String> event) {
                itemsLw.getItems().set(event.getIndex(), event.getNewValue());
                enableSave();
            }
        });
    }
    
    /**
     * Enables the save button.
     */
    @FXML private void enableSave() {
        saveBtn.setDisable(false);
    }
    
    /**
     * Displays the "Duplicate List Name" text for 3 seconds.
     */
    private void startErrorTxt() {
        dupErrorTxt.setVisible(true);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                dupErrorTxt.setVisible(false);
                timer.cancel();
            }
        }, 3000);
    }
    
    /**
     * Adds a new list and selects it.
     */
    @FXML private void handleAddList() {
        if (!newListTf.getText().isEmpty()) {
            if (!listManager.isList(newListTf.getText())) {
                listManager.addList(newListTf.getText());
                newListTf.clear();
                listsLw.getSelectionModel().select(listManager.getSize()-1);
                handleSelectList();
                enableSave();
            }
            else {
                startErrorTxt();
            }
        }
    }
    
    /**
     * Removes a list and all it's contents. If the list to be deleted
     * has content, the user is first asked for confirmation before
     * deletion.
     */
    @FXML private void handleRemoveList() {
        if (getSelectedList() != null) {
            if (getSelectedList().isEmpty()) {
                listManager.removeList(listsLw.getSelectionModel().getSelectedIndex());
                itemsLw.getItems().clear();
                listsLw.getSelectionModel().clearSelection();
                listsLw.getSelectionModel().select(listManager.getSize() - 1);
                handleSelectList();
                enableSave();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("List Manager");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to delete list " + getSelectedList().getName() + "? \nAll contens of this list will also be deleted");
                ButtonType yesBtn = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
                ButtonType noBtn = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
                alert.getButtonTypes().setAll(yesBtn, noBtn);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == yesBtn) {
                    listManager.removeList(listsLw.getSelectionModel().getSelectedIndex());
                    itemsLw.getItems().clear();
                    listsLw.getSelectionModel().clearSelection();
                    listsLw.getSelectionModel().select(listManager.getSize() - 1);
                    handleSelectList();
                    enableSave();
                }
            }
        }
    }
    
    /**
     * Returns the selected list or null if no list selected.
     * 
     * @return the selected list
     */
    private List getSelectedList() {
        if (listsLw.getSelectionModel().isEmpty()) return null;
        return listManager.getList(listsLw.getSelectionModel().getSelectedIndex());
    }
    
    /**
     * Calls the handleAddList() function if the enter key has
     * been pressed.
     * 
     * @param event a keypress
     */
    @FXML private void handleEnterList(KeyEvent event) {
    if (event.getCode() == KeyCode.ENTER) handleAddList();
    }
    
    /**
     * Displays the selected list and enables the appropriate buttons.
     * If no list is selected, it will disable the buttons and show
     * a placeholder instead of showing any items.
     */
    @FXML private void handleSelectList() {
        if (getSelectedList() != null) {
            setDisableItems(false);
            removeListBtn.setDisable(false);
            itemsPlaceholder.setText("No Items in this List");
            itemsLw.setItems(getSelectedList().getItems());
            itemsLw.getSelectionModel().clearSelection();
        }
        else {
            setDisableItems(true);
            removeListBtn.setDisable(true);
            itemsPlaceholder.setText("No List Selected");
            itemsLw.setItems(null);
        }
    }
    
    /**
     * Disables or enables the items section of the screen.
     * 
     * @param bool true to disable all buttons, false to enable all buttons
     */
    private void setDisableItems(boolean bool) {
        itemsLw.setDisable(bool);
        addItemBtn.setDisable(bool);
        newItemTf.clear();
        newItemTf.setDisable(bool);
    }
    
    /**
     * Adds a new item from the input box to the selected list.
     */
    @FXML private void handleAddItem() {
        if (!newItemTf.getText().isEmpty() && getSelectedList() != null) {
            getSelectedList().addItem(newItemTf.getText());
            newItemTf.clear();
            itemsLw.getSelectionModel().clearSelection();
            enableSave();
        }
    }
    
    /**
     * Removes the selected item from the list.
     */
    @FXML private void handleRemoveItem() {
        if (!itemsLw.getSelectionModel().isEmpty()) {
            getSelectedList().removeItem(itemsLw.getSelectionModel().getSelectedIndex());
            itemsLw.getSelectionModel().clearSelection();
            enableSave();
        }
    }
    
    /**
     * Calls the handleAddItem() function if the enter key has
     * been pressed.
     * 
     * @param event a keypress
     */
    @FXML private void handleEnterItem(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) handleAddItem();
    }
    
    /**
     * Saves the program.
     */
    @FXML private void saveProgram() {
        try {
            listManager.saveAll();
            saveBtn.setDisable(true);
        } catch (IOException ex) {
            exceptionAlert(ex.getMessage());
        }
    }
    
    /**
     * Exits the program. If there are unsaved items, the user is notified
     * and has the option to save before exiting.
     */
    @FXML public void exitProgram() {
        if (saveBtn.isDisabled()) {
            Platform.exit();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("List Manager");
            alert.setHeaderText(null);
            alert.setContentText("There are unsaved changes.\nWould you like to save before exiting?");
            ButtonType yesBtn = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
            ButtonType noBtn = new ButtonType("No", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelBtn = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            alert.getButtonTypes().setAll(yesBtn, noBtn, cancelBtn);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == yesBtn) {
                saveProgram();
                Platform.exit();
            }
            if (result.get() == noBtn) {
                Platform.exit();
            }
        }
    }
    
    /**
     * Displays an error to the user informing them of an exception that
     * has occurred.
     * 
     * @param message the error message
     */
    private void exceptionAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("An error occured");
        alert.setHeaderText("A file error occured");
        alert.setContentText("Error message:\n---------\n" + message +"\n---------\nWe recommend you restart the program immediately.");
        alert.showAndWait();
    }
}
