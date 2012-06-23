/*
 * Automatic Routine Management System
 */
package arms;

import java.io.Serializable;

/**
 *
 * @author vh4x0r
 */
public class Subject implements Serializable {

    public Subject(String name, String id) {
        this.name = name;
        this.id = id;
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
        Subject other = (Subject) otherObject;
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

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return name;
    }
    private String name;
    private String id;
}
