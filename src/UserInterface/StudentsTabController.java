package UserInterface;

import controllers.TeacherHandler;
import businessLayer.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import java.util.List;

public class StudentsTabController {

    private TeacherHandler teacherHandler;

    public void setTeacherHandler(TeacherHandler handler) {
        this.teacherHandler = handler;
        loadStudents();
    }

    @FXML
    private Button RemoveStudent;

    @FXML
    private TableView<Student> lst_Students;
    
    @FXML
    private TableColumn<Student, String> name_t;

    @FXML
    private TableColumn<Student, String> username_t;

    @FXML
    void handle_RemoveStudent(MouseEvent event) {
        // Implement removal logic if needed.
    }
    
    private void loadStudents() {
        if (teacherHandler != null && teacherHandler.getCurrentClass() != null) {
            String classCode = teacherHandler.getCurrentClass().getClassCode();
            // Use TeacherHandler's method to fetch students in the class.
            List<Student> students = teacherHandler.getStudentsInTheClass(classCode);
            ObservableList<Student> studentList = FXCollections.observableArrayList(students);
            lst_Students.setItems(studentList);
            
            // Set up table columns using property names that match your Student class getters.
            name_t.setCellValueFactory(new PropertyValueFactory<>("name"));
            username_t.setCellValueFactory(new PropertyValueFactory<>("username"));
        }
    }
}
