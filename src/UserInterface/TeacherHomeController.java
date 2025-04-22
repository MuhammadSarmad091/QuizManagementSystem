package UserInterface;

import java.io.IOException;
import java.util.List;
import businessLayer.Class;
import businessLayer.User;
import controllers.TeacherHandler;
import dbHandlers.classDBH;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class TeacherHomeController {

    private TeacherHandler teacherHandler;  // Holds the logged-in user

    @FXML
    private AnchorPane CenterPane;

    @FXML
    private TextField NameText;

    @FXML
    private Button SignOutbutton;

    @FXML
    private TextField UsernameText;

    @FXML
    private Button Home;
    
    @FXML
    private ImageView profile_img;
    
    // This initialize method is automatically called by the FXML loader.
    @FXML
    public void initialize() {
        // You can do general initialization here if needed.
        // Do NOT use loggedUser here since it may not be set yet.
    }

    // Custom initialization method to set user-specific data.
    public void initData(TeacherHandler handler) {
        this.teacherHandler = handler;
        // Now update UI elements with user data.
        if (teacherHandler != null) {
            UsernameText.setText(teacherHandler.getTeacher().getUsername());
            NameText.setText(teacherHandler.getTeacher().getName());
        }
        loadDataFromDatabase();
    }
    
    private Object openCreateClassWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/NewClass.fxml"));
            Parent newClassRoot = loader.load();
            
            NewClassController controller = loader.getController();
            if (controller != null) {
                controller.initData(teacherHandler);
            }
            
            // Load FXML inside CenterPane without changing the whole scene.
            StackPane wrapper = new StackPane(newClassRoot);
            wrapper.setAlignment(Pos.CENTER);
            CenterPane.getChildren().clear();
            CenterPane.getChildren().add(wrapper);
            wrapper.prefWidthProperty().bind(CenterPane.widthProperty());
            wrapper.prefHeightProperty().bind(CenterPane.heightProperty());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    private Object openCourseDetails(String courseName, String courseCode) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/DisplayCLass.fxml"));
            Parent newClassRoot = loader.load();
            
            // Get the DisplayClassController instance
            DisplayClassController controller = loader.getController();
            if (controller != null) {
                controller.initialize(courseName, courseCode);
                Class class1 = new Class();
                class1.setClassCode(courseCode);
                class1.setClassName(courseName);
                teacherHandler.setCurrentClass(class1);
                controller.initData(teacherHandler);
            }
            
            AnchorPane wrapper = new AnchorPane(newClassRoot);
            AnchorPane.setTopAnchor(newClassRoot, 20.0);
            CenterPane.getChildren().clear();
            CenterPane.getChildren().add(wrapper);
            wrapper.prefWidthProperty().bind(CenterPane.widthProperty());
            wrapper.prefHeightProperty().bind(CenterPane.heightProperty());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    
    @FXML
    void HandleHome(MouseEvent event) {
        ReloadPage();
    }
    
    private void ReloadPage() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/TeacherHome.fxml"));
            Parent teacherHomeRoot = loader.load();
            
            TeacherHomeController controller = loader.getController();
            if (controller != null) {
                controller.initData(teacherHandler);
            }
            
            Stage stage = (Stage) Home.getScene().getWindow();
            stage.setScene(new Scene(teacherHomeRoot));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    void SignOutbutton(MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UserInterface/Login.fxml"));
            Parent loginRoot = loader.load();
            Stage stage = (Stage) SignOutbutton.getScene().getWindow();
            stage.setScene(new Scene(loginRoot));
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR, "Error while signing out.");
            alert.showAndWait();
        }
    }
    
    // Modified loadDataFromDatabase: fetch classes from the database using loggedUser's username.
    private void loadDataFromDatabase() {
        List<Class> classes = classDBH.getDBH().getClasses(teacherHandler.getTeacher().getUsername());
        GridPane gridPane = new GridPane();
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setStyle("-fx-background-color: #ffffff; -fx-padding: 20px; -fx-border-radius: 10px; -fx-border-color: #cccccc;");
        gridPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        for (int i = 0; i < 3; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setHgrow(Priority.ALWAYS);
            colConst.setPercentWidth(100.0 / 3);
            gridPane.getColumnConstraints().add(colConst);
        }
        int row = 0, col = 0;
        if (classes != null && !classes.isEmpty()) {
            for (Class cls : classes) {
                String courseName = cls.getClassName();
                String courseCode = cls.getClassCode();
                
                VBox courseBox = new VBox(8);
                courseBox.setStyle("-fx-border-color: #444; -fx-border-width: 1px; " +
                        "-fx-background-color: linear-gradient(to bottom, #f8f9fa, #e9ecef); " +
                        "-fx-background-radius: 10px; -fx-border-radius: 10px; " +
                        "-fx-padding: 15px; -fx-alignment: center;");
                courseBox.setMinSize(180, 120);
                courseBox.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                courseBox.setOnMouseClicked(event -> openCourseDetails(courseName, courseCode));
                
                Label courseLabel = new Label(courseName);
                courseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
                courseLabel.setStyle("-fx-text-fill: #333;");
                
                Label codeLabel = new Label(courseCode);
                codeLabel.setFont(Font.font("Arial", 12));
                codeLabel.setStyle("-fx-text-fill: gray;");
                
                courseBox.getChildren().addAll(courseLabel, codeLabel);
                GridPane.setMargin(courseBox, new Insets(20));
                gridPane.add(courseBox, col, row);
                col++;
                if (col > 2) {
                    col = 0;
                    row++;
                }
            }
        } else {
            Label noClassesLabel = new Label("No classes available.");
            noClassesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 16));
            gridPane.add(noClassesLabel, 0, 0);
        }
        
        Button createClassButton = new Button("+ Create New Class");
        createClassButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-font-size: 14px; -fx-padding: 10px 15px; -fx-border-radius: 5px;");
        createClassButton.setMinSize(180, 40);
        createClassButton.setOnAction(event -> openCreateClassWindow());
        
        VBox buttonBox = new VBox(createClassButton);
        buttonBox.setAlignment(Pos.CENTER);
        GridPane.setMargin(buttonBox, new Insets(40));
        gridPane.add(buttonBox, col, row);
        
        StackPane stackPane = new StackPane(gridPane);
        stackPane.setAlignment(Pos.CENTER);
        CenterPane.getChildren().clear();
        CenterPane.getChildren().add(stackPane);
        stackPane.prefWidthProperty().bind(CenterPane.widthProperty());
        stackPane.prefHeightProperty().bind(CenterPane.heightProperty());
    }
}
