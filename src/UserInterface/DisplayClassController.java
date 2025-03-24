package UserInterface;

import controllers.TeacherHandler;
import java.io.IOException;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DisplayClassController {
    
    private TeacherHandler teacherHandler; // New field to hold TeacherHandler
    
    // This method now accepts teacherHandler
    public void initData(TeacherHandler handler) {
        this.teacherHandler = handler;
        // Set up the listener for tab selection changes
        TabPane_Sec.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> obs, Tab oldTab, Tab newTab) {
                if (newTab != null) {
                    loadFXMLForTab(newTab);
                }
            }
        });
        // Load default tab content (if any)
        Tab defaultTab = TabPane_Sec.getSelectionModel().getSelectedItem();
        if (defaultTab != null) {
            loadFXMLForTab(defaultTab);
        }
    }

    // Existing initialize method to set class info.
    public void initialize(String ClassName, String ClassCode) {
        classname.setText(ClassName);
        classcode.setText(ClassCode);
    }

    @FXML
    private TextField classname;

    @FXML
    private TextField classcode;
    
    @FXML
    private TabPane TabPane_Sec;
    
    @FXML
    private Button Add_teacher;
    
    @FXML
    private Button remove_class;
    
    // Modified loadFXMLForTab: loads the corresponding FXML and passes teacherHandler to the tab controller.
    private void loadFXMLForTab(Tab tab) {
        String tabTitle = tab.getText(); // Get the tab's title
        String fxmlFile = "";
        switch (tabTitle) {
            case "Quizzes":
                fxmlFile = "QuizesTab.fxml";
                break;
            case "Students":
                fxmlFile = "StudentsTab.fxml";
                break;
            case "Teachers":
                fxmlFile = "TeachersTab.fxml";
                break;
            default:
                return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent newContent = loader.load();
            
            Object controller = loader.getController();
            if (controller instanceof QuizesTabController) {
                ((QuizesTabController) controller).initData(teacherHandler);
                System.out.println("Loaded QuizesTabController with teacherHandler: " + teacherHandler);
            } else if (controller instanceof StudentsTabController) {
                ((StudentsTabController) controller).setTeacherHandler(teacherHandler);
            } else if (controller instanceof TeachersTabController) {
                ((TeachersTabController) controller).setTeacherHandler(teacherHandler);
            }
            
            AnchorPane wrapper = new AnchorPane(newContent);
            AnchorPane.setTopAnchor(newContent, 0.0);
            AnchorPane.setBottomAnchor(newContent, 0.0);
            AnchorPane.setLeftAnchor(newContent, 0.0);
            AnchorPane.setRightAnchor(newContent, 0.0);
            tab.setContent(wrapper);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void handle_Add_teacher(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/AddTeacherCLass.fxml"));
            Parent root = loader.load();

            // Get controller instance
            AddTeacherClassController controller = loader.getController();
            if (controller != null) {
                controller.initialize();
            }

            // Create new stage
            Stage stage = new Stage();
            stage.setTitle("Check Objective Submission");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void handle_remove_class(MouseEvent event) {
        // Implementation for removal if needed.
    }
}
