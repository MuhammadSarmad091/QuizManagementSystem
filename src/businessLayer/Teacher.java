package businessLayer;

public class Teacher extends User {
    public Teacher() {
        super();
    }
    
    public Teacher(String username, String password, String type) {
        super(username, password, type);
    }

    public Teacher(String username, String password, String type, String name) {
        super(username, password, type, name);
    }
}