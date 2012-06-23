/*
 * Automatic Routine Management System
 */
package arms;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ArrayList;

/**
 *
 * @author vh4x0r
 */
public class Teacher implements Serializable {

    public Teacher(String name, String id, String pass) {
        this.name = name;
        this.id = id;
        this.pass = pass.hashCode();
        subjectList = new ArrayList<Subject>();
        lastLogin = new Date();
    }

    @Override
    public boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null) {
            return false;
        }
        if (getClass() != otherObject.getClass()) {
            return false;
        }
        Teacher other = (Teacher) otherObject;
        if (other.getId().equalsIgnoreCase(id)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }

    public boolean validate(String id, String pass) {
        if (id.equals(this.id) && pass.hashCode() == this.pass) {
            return true;
        }
        return false;
    }

    public void setLogin(Date d) {
        lastLogin = d;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getLogin() {
        return new SimpleDateFormat().format(lastLogin);
    }

    public ArrayList<Subject> getSubjectList() {
        return subjectList;
    }

    public void addSubject(Subject s) {
        subjectList.add(s);
    }

    public void removeSubject(int index) {
        subjectList.remove(index);
    }

    public void moveUp(int index) {
        if (index == 0) {
            return;
        }
        Subject temp = subjectList.get(index);
        subjectList.set(index, subjectList.get(index - 1));
        subjectList.set(index - 1, temp);
    }

    public void moveDown(int index) {
        if (index == subjectList.size() - 1) {
            return;
        }
        Subject temp = subjectList.get(index);
        subjectList.set(index, subjectList.get(index + 1));
        subjectList.set(index + 1, temp);
    }

    @Override
    public String toString() {
        return name;
    }
    private String name;
    private String id;
    private long pass;
    private ArrayList<Subject> subjectList;
    private Date lastLogin;
}
