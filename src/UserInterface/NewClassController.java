package UserInterface;

import java.io.IOException;
import java.util.regex.Pattern;
import controllers.TeacherHandler;
import dbHandlers.classDBH;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class NewClassController {

    // Field to receive TeacherHandler from TeacherHomeController.
    private TeacherHandler teacherHandler;

    @FXML
    private TextField class_code;

    @FXML
    private TextArea class_descrip;

    @FXML
    private TextField class_name;

    @FXML
    private Button create_class_btn;

    @FXML
    private Button go_back;

    // This initialize method is automatically called by the FXML loader.
    // We now use the teacherHandler to generate a unique class code.
    public void initialize() {
        // ReloadPage now uses generateClassCode from TeacherHandler.
        ReloadPage();
    }

    // Custom initialization to receive TeacherHandler.
    public void initData(TeacherHandler handler) {
        this.teacherHandler = handler;
        ReloadPage();
    }

    // Called when Create Class button is clicked.
    @FXML
    void handle_create_class_btn(MouseEvent event) {
        // Retrieve input values.
        String code = class_code.getText().trim();
        String name = class_name.getText().trim();
        String description = class_descrip.getText().trim();

        // Validate: class name and description cannot be empty.
        if (name.isEmpty() || description.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Class name and description cannot be empty.");
            return;
        }

        // Validate: class name should be only alphabets and spaces.
        // Regex: one or more letters or spaces.
        if (!Pattern.matches("^[A-Za-z ]+$", name)) {
            showAlert(Alert.AlertType.ERROR, "Input Error", "Class name must contain only alphabets and spaces.");
            return;
        }

        // Use teacherHandler to create the class.
        teacherHandler.createClass(code, name, description);

        // Add the teacher in the class. Teacher ID is obtained from teacherHandler.
        boolean added = teacherHandler.addTeacherInClass(teacherHandler.getTeacher().getUsername(), code);
        if (!added) {
            showAlert(Alert.AlertType.ERROR, "Error", "Class created, but failed to add teacher to class.");
            return;
        }

        // Show success message.
        showAlert(Alert.AlertType.INFORMATION, "Success", "Class created successfully!");

        // After creation, reload TeacherHome.
        loadTeacherMenu();
    }

    // Reset the input fields.
    private void ReloadPage() {
        if (teacherHandler != null) {
            // Generate a new class code using teacherHandler's generateClassCode.
            class_code.setText(teacherHandler.generateClassCode());
        }
        class_name.setText("");
        class_descrip.setText("");
    }



    // Back button action: load TeacherHome properly with the teacherHandler.
    @FXML
    void handle_go_back(MouseEvent event) {
        loadTeacherMenu();
    }

    // Loads the TeacherHome page, passing the TeacherHandler.
    private void loadTeacherMenu() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/TeacherHome.fxml"));
            Parent teacherHomeRoot = loader.load();

            // Get the controller instance and pass the teacherHandler.
            TeacherHomeController controller = loader.getController();
            if (controller != null) {
                controller.initData(teacherHandler);
            }

            // Get the current stage and set the new scene.
            Stage stage = (Stage) go_back.getScene().getWindow();
            stage.setScene(new Scene(teacherHomeRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Helper method to show alerts.
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
