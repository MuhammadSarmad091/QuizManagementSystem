package UserInterface;

import controllers.StudentHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.function.UnaryOperator;
import javafx.scene.control.TextFormatter.Change;

public class JoinClassController {

    private StudentHandler studentHandler;  // Holds the handler passed from previous view

    @FXML
    private TextField class_code;

    @FXML
    private Button go_back;

    @FXML
    private Button join_class_btn;

    /**
     * Called by previous controller to pass StudentHandler.
     */
    public void initData(StudentHandler handler) {
        this.studentHandler = handler;
    }

    @FXML
    public void initialize() {
        // Only allow alphanumeric input
        UnaryOperator<Change> filter = change -> {
            String text = change.getControlNewText();
            return text.matches("[a-zA-Z0-9]*") ? change : null;
        };
        class_code.setTextFormatter(new TextFormatter<>(filter));
        clearInput();
    }

    @FXML
    void handle_go_back(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/StudentHome.fxml"));
            Parent root = loader.load();

            StudentHomeController controller = loader.getController();
            if (controller != null) {
                controller.initData(studentHandler);
            }

            Stage stage = (Stage) go_back.getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Unable to return to Student Home").showAndWait();
        }
    }

    @FXML
    void handle_join_class_btn(MouseEvent event) {
        String code = class_code.getText().trim();
        if (code.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "Please enter a class code.").showAndWait();
            return;
        }
        boolean joined = studentHandler.joinClass(code);
        if (joined) {
            new Alert(Alert.AlertType.INFORMATION, "Class joined successfully.").showAndWait();
            clearInput();
        } else {
            new Alert(Alert.AlertType.WARNING, "Either Class Code is incorrect or you are already in this class.").showAndWait();
        }
    }

    private void clearInput() {
        class_code.setText("");
    }
}
