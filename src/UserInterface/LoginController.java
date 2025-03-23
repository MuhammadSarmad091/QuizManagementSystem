package UserInterface;

import java.io.IOException;
import dbHandlers.userDBH;
import businessLayer.User;
import controllers.TeacherHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private Button loginButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label signup;

    @FXML
    private TextField usernameField;

    @FXML
    void handleLoginButtonAction(MouseEvent event) {
        // Get user inputs
        String username = usernameField.getText();
        String password = passwordField.getText();
        
        // Call the login function from the DB handler
        User user = userDBH.getDBH().logIn(username, password);
        TeacherHandler handler = new TeacherHandler();
        handler.setTeacher(user);
        if (user != null) {
            // If login successful, load TeacherHome page and pass the user object.
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/TeacherHome.fxml"));
                Parent teacherHomeRoot = loader.load();

                // Get the controller instance and set the logged-in user.
                TeacherHomeController controller = loader.getController();
                controller.initData(handler); // Optionally call initialize if needed

                // Get the current stage and set the new scene.
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(teacherHomeRoot));
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            // If login fails, show an error alert.
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login Error");
            alert.setHeaderText(null);
            alert.setContentText("Invalid username or password.");
            alert.showAndWait();
        }
    }

    @FXML
    void handleSignupAction(MouseEvent event) {    
        loadSignUpMenu();
    }

    private void loadSignUpMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/SignUp.fxml"));
            Parent teacherHomeRoot = loader.load();

            // Get the current stage
            Stage stage = (Stage) loginButton.getScene().getWindow();
            // Set the new scene
            stage.setScene(new Scene(teacherHomeRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
