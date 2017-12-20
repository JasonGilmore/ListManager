package Application;


import controller.ListController;
import javafx.application.Application;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.stage.Stage;
import javafx.fxml.*;
import javafx.scene.*;

/**
 * @author Jason Gilmore
 * 
 * The class that launches the FX application.
 */
public class ListApplication extends Application {
    
    /**
     * A fallback in case the application is not launched.
     * 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Set up and load the window.
     * 
     * @param stage       stage
     * @throws Exception  exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ListView.fxml"));
        Parent root = loader.load();
        stage.setTitle("List Manager");
        stage.setScene(new Scene(root));
        stage.show();
        stage.setOnCloseRequest(new EventHandler() {
            @Override
            public void handle(Event event) {
                ListController controller = loader.getController();
                controller.exitProgram();
                event.consume();
            }
        });
    }
}
