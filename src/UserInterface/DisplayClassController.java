package UserInterface;

import controllers.TeacherHandler;
import java.io.IOException;
import java.util.Optional;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class DisplayClassController {
    
    private TeacherHandler teacherHandler;
    
    public void initData(TeacherHandler handler) {
        this.teacherHandler = handler;
        TabPane_Sec.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<Tab>() {
            @Override
            public void changed(ObservableValue<? extends Tab> obs, Tab oldTab, Tab newTab) {
                if (newTab != null) {
                    loadFXMLForTab(newTab);
                }
            }
        });
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
    
    private void loadFXMLForTab(Tab tab) {
        String tabTitle = tab.getText();
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

            // Get controller instance and pass the teacherHandler
            AddTeacherClassController controller = loader.getController();
            controller.initData(teacherHandler);

            // Create new stage and show the view
            Stage stage = new Stage();
            stage.setTitle("Add Teacher To Class");
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void handle_remove_class(MouseEvent event) 
    {
        // Show confirmation dialog
        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirm Deletion");
        confirmationAlert.setHeaderText("Delete Class");
        confirmationAlert.setContentText("Do you really want to delete this class?");
        
        Optional<ButtonType> result = confirmationAlert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Remove class using teacherHandler
            teacherHandler.removeClass(teacherHandler.getCurrentClass().getClassCode());

            // Load TeacherHome page
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/TeacherHome.fxml"));
                Parent teacherHomeRoot = loader.load();

                // Get the controller instance and set the logged-in user
                TeacherHomeController controller = loader.getController();
                controller.initData(teacherHandler);

                // Get current stage and replace scene
                Stage stage = (Stage) remove_class.getScene().getWindow();
                stage.setScene(new Scene(teacherHomeRoot));
                stage.setTitle("Teacher Home");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
