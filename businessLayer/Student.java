package businessLayer;

public class Student extends User {
    public Student() {
        super();
    }
    
    public Student(String username, String password, String type) {
        super(username, password, type);
    }

    public Student(String username, String password, String type, String name) {
        super(username, password, type, name);
    }
}
