package logic;

import java.io.Serializable;

public abstract class Person implements Serializable {
    protected String name;
    protected int age;
    protected String role;
    protected int personId;
    private static int nextId = 1;

    public Person(String name, int age, String role) {
        this.name = name;
        this.age = age;
        this.role = role;
        this.personId = nextId++;
    }

    public int getPersonId() { 
        return personId; 
    }
    
    public void setPersonId(int id) {
        this.personId = id;
        // Обновляем nextId, если новый ID больше текущего
        if (id >= nextId) {
            nextId = id + 1;
        }
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