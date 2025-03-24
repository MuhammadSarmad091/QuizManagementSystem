package UserInterface;
import controllers.TeacherHandler;
import businessLayer.Teacher;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.util.List;

public class TeachersTabController {

    private TeacherHandler teacherHandler;

    public void setTeacherHandler(TeacherHandler handler) {
        this.teacherHandler = handler;
        loadTeachers();
    }

    @FXML
    private Button RemoveTeacher;

    @FXML
    private TableView<Teacher> lst_Teachers;
    
    @FXML
    private TableColumn<Teacher, String> name_t;

    @FXML
    private TableColumn<Teacher, String> username_t;

    @FXML
    void handle_RemoveTeacher(MouseEvent event) {
        // Implement removal logic if needed.
    }
    
    private void loadTeachers() {
        if (teacherHandler != null && teacherHandler.getCurrentClass() != null) {
            String classCode = teacherHandler.getCurrentClass().getClassCode();
            List<Teacher> teachers = teacherHandler.getTeachersInTheClass(classCode);
            ObservableList<Teacher> teacherList = FXCollections.observableArrayList(teachers);
            lst_Teachers.setItems(teacherList);
            
            name_t.setCellValueFactory(new PropertyValueFactory<>("name"));
            username_t.setCellValueFactory(new PropertyValueFactory<>("username"));
        }
    }
}
