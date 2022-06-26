package Logic;

import java.util.Objects;

public class Student {
    private final String id;
    private final String firstName;
    private final String lastName;

    public Student(String id,String firstName,String lastName){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id='" + id + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Student student)) return false;
        return Objects.equals(getId(), student.getId()) && Objects.equals(getFirstName(), student.getFirstName()) && Objects.equals(getLastName(), student.getLastName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getFirstName(), getLastName());
    }
}
