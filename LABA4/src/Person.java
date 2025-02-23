import java.io.Serializable;

public abstract class Person implements Serializable {
    protected String name;
    protected int age;
    protected String role;
    protected int personId;
    private static int nextId = 1; // Статическое поле для генерации ID

    public Person(String name, int age, String role) {
        this.name = name;
        this.age = age;
        this.role = role;
        this.personId = nextId++;
    }

    public int getPersonId() { 
        return personId; 
    }

    // Абстрактный метод для взаимодействия
    public abstract void interactWithPatient();

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    @Override
    public String toString() {
        return "ID: " + personId + ", Имя: " + name + ", Возраст: " + age + ", Роль: " + role;
    }
}