package UserInterface;

import java.io.IOException;
import java.util.Optional;

import controllers.StudentHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class OpenClassController {
	StudentHandler studentHandler;
	
	public void initData(StudentHandler handler)
	{
		this.studentHandler = handler;
		this.loadTabPane();
	}
	
	public void initialize(String ClassName , String ClassCode) 
	{
		classname.setText(ClassName);
		classcode.setText(ClassCode);  
		
		//loadTabPane();
    }

	@FXML
    private TabPane TabPane_Sec;

    @FXML
    private TextField classcode;

    @FXML
    private TextField classname;

    @FXML
    private Button leave_class;

    @FXML
    void handle_leave_class(MouseEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION, 
            "Are you sure you want to leave this class?\nThis will delete all your submissions!");
        alert.setHeaderText(null);
        alert.setTitle("Confirm Leave Class");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            studentHandler.leaveClass();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/StudentHome.fxml"));
                Parent root = loader.load();

                StudentHomeController controller = loader.getController();
                if (controller != null) {
                    controller.initData(studentHandler);
                }

                // Get current stage and set new scene
                Stage currentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                currentStage.setScene(new Scene(root));
                currentStage.setTitle("Student Home");
                currentStage.show();
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    
    
    private void loadTabPane() {
        TabPane tabPane = TabPane_Sec;

        if (tabPane != null) {
            for (Tab tab : tabPane.getTabs()) {
                tab.setOnSelectionChanged(event -> {
                    if (tab.isSelected()) {
                        loadFXMLForTab(tab);
                    }
                });
            }
        }
        loadFXMLForTab(tabPane.getTabs().getFirst());
    }

    private void loadFXMLForTab(Tab tab) 
    {

        String tabTitle = tab.getText(); // Get the tab's title
        String fxmlFile = "";

        switch (tabTitle) {
            case "Quizzes":
                fxmlFile = "QuizesTab2.fxml";
                break;
            default:
                return;
        }

        try {
            // Load the corresponding FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent newContent = loader.load();
            
            QuizesTab2Controller controller = loader.getController();
            controller.initData(studentHandler);

            // Create a wrapper StackPane to contain the loaded content
            StackPane wrapper = new StackPane();
            
            // Add a ScrollPane to handle any overflow
            ScrollPane scrollPane = new ScrollPane(newContent);
            scrollPane.setFitToWidth(true);  // Ensures horizontal fit
            scrollPane.setFitToHeight(true); // Ensures vertical fit
            
            // Add ScrollPane inside the wrapper
            wrapper.getChildren().add(scrollPane);

            // Bind wrapper size to TabPane's size
            TabPane tabPane = tab.getTabPane();
            wrapper.prefWidthProperty().bind(tabPane.widthProperty());
            wrapper.prefHeightProperty().bind(tabPane.heightProperty());

            // Set wrapped content in the tab
            tab.setContent(wrapper);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
