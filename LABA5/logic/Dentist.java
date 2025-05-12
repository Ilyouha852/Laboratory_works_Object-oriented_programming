package logic;

public class Dentist extends Person implements MedicalStaff {
    private String specialization;

    public Dentist(String name, int age, String specialization) {
        super(name, age, "Стоматолог");
        this.specialization = specialization;
    }

    @Override
    public void interactWithPatient() {
        System.out.println("Стоматолог " + name + " (" + specialization + ") проводит осмотр");
    }

    @Override
    public void performDuties() {
        System.out.println("Стоматолог " + name + " (" + specialization + ") проводит лечение");
    }

    public String getSpecialization() { 
        return specialization; 
    }
    
    public void setSpecialization(String specialization) { 
        this.specialization = specialization; 
    }


    @Override
    public String toString() {
        return String.format("Стоматолог [ID: %d, Имя: %s, Специализация: %s]", 
        personId, name, specialization);
    }
}