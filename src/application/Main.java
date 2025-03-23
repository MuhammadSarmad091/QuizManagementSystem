package application;
	
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) {
        try {
            // Load FXML file properly
            Parent root = FXMLLoader.load(getClass().getResource("/UserInterface/Login.fxml"));
            // Create scene
            Scene scene = new Scene(root);
            // Set scene to stage
            primaryStage.setScene(scene);      
            // Set title (optional)
            primaryStage.setTitle("Examify");
      
            // Set full-screen or maximized window
            //primaryStage.setMaximized(true); // Maximized window
            
            // Show stage
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
