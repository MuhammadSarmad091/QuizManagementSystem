package UserInterface;

import java.io.IOException;
import dbHandlers.userDBH;
import businessLayer.User;
import controllers.TeacherHandler;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class SignUpController {

    @FXML
    private TextField Name;

    @FXML
    private Button Signupbtn;

    @FXML
    private Label login;

    @FXML
    private PasswordField passwordField;

    @FXML
    private TextField usernameField;
    
    @FXML
    private ComboBox<String> typeSelectCB; // ComboBox for user type

    // Initialize method to populate the ComboBox
    @FXML
    public void initialize() {
        // Add "Teacher" and "Student" items; prompt set to "Select"
        typeSelectCB.setItems(FXCollections.observableArrayList("Teacher", "Student"));
        typeSelectCB.setPromptText("Select");
    }
    
    @FXML
    void handleSignUp(MouseEvent event) {
        // Retrieve user inputs
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        String name = Name.getText().trim();
        String type = typeSelectCB.getValue(); // Should be "Teacher" or "Student"
        
        // Validate that username, password, and name are not empty
        if(username.isEmpty() || password.isEmpty() || name.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Sign Up Error", "Username, Password, and Name cannot be empty.");
            return;
        }
        
        // Validate that a user type is selected
        if (type == null || type.equals("Select")) {
            showAlert(Alert.AlertType.ERROR, "Sign Up Error", "Please select a valid user type.");
            return;
        }
        
        // Validate name: must contain only alphabets with exactly one space (e.g., "John Doe")
        if (!name.matches("^[A-Za-z]+\\s[A-Za-z]+$")) {
            showAlert(Alert.AlertType.ERROR, "Sign Up Error", "Name must contain only alphabets with exactly one space (full name).");
            return;
        }
        
        // Call the signUp function from the userDBH class
        User newUser = userDBH.getDBH().signUp(username, password, name, type);
        
        if (newUser != null) {
            // Sign up successful
            if (newUser.getType().equalsIgnoreCase("teacher")) {
                loadTeacherHome(newUser);
            } else if (newUser.getType().equalsIgnoreCase("student")) {
                // TODO: Handle student home navigation later.
                showAlert(Alert.AlertType.INFORMATION, "Sign Up", "Student sign up successful. Student home screen to be implemented.");
            }
        } else {
            // Sign up failed: either username exists or other error occurred.
            showAlert(Alert.AlertType.ERROR, "Sign Up Error", "Sign up failed. Username may already exist or an error occurred.");
        }
    }
    
    private void loadTeacherHome(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/TeacherHome.fxml"));
            Parent teacherHomeRoot = loader.load();
            TeacherHandler handler = new TeacherHandler();
            handler.setTeacher(user);
            
            // Pass the logged-in user to TeacherHomeController
            TeacherHomeController controller = loader.getController();
            controller.initData(handler);
            
            // Get the current stage and load the new scene
            Stage stage = (Stage) Signupbtn.getScene().getWindow();
            stage.setScene(new Scene(teacherHomeRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadLoginPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/Login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) Name.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handleloginAction(MouseEvent event) {
        loadLoginPage();
    }
    
    // Helper method to show alerts
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
