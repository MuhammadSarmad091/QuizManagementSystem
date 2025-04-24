package UserInterface;

import java.io.IOException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;

public class OpenClassController {
	
	public void initialize(String ClassName , String ClassCode) 
	{
		classname.setText(ClassName);
		classcode.setText(ClassCode);  
		
		loadTabPane();
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
    void handle_leave_class(MouseEvent event) 
    {
    		
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
