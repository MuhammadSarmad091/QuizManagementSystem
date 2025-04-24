package UserInterface;

import java.io.IOException;
import dbHandlers.userDBH;
import businessLayer.User;
import controllers.StudentHandler;
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
import javafx.scene.control.TextFormatter;
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

        if (user != null) {
            try {
                Stage stage = (Stage) loginButton.getScene().getWindow();
                FXMLLoader loader;
                Parent root;

                if (user.getType().equalsIgnoreCase("Teacher")) {
                    // Load Teacher UI
                    TeacherHandler handler = new TeacherHandler();
                    handler.setTeacher(user);
                    loader = new FXMLLoader(getClass().getResource("/UserInterface/TeacherHome.fxml"));
                    root = loader.load();
                    TeacherHomeController controller = loader.getController();
                    controller.initData(handler);
                } else if (user.getType().equalsIgnoreCase("Student")) {
                    // Load Student UI
                    StudentHandler handler = new StudentHandler();
                    handler.setStudent(user);
                    loader = new FXMLLoader(getClass().getResource("/UserInterface/StudentHome.fxml"));
                    root = loader.load();
                    StudentHomeController controller = loader.getController();
                    controller.initData(handler);
                } else {
                    throw new Exception("Unsupported user type: " + user.getType());
                }

                stage.setScene(new Scene(root));
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Login Error");
                alert.setHeaderText("Error during login");
                alert.setContentText("Could not load UI: " + e.getMessage());
                alert.showAndWait();
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
    
    @FXML
    public void initialize() 
    {
        // Allow only alphabets and digits in the usernameField
    	usernameField.setTextFormatter(new TextFormatter<>(change -> {
            String newText = change.getControlNewText();
            if (newText.matches("[a-zA-Z0-9]*")) {
                return change;
            }
            return null;
        }));
    }
}
