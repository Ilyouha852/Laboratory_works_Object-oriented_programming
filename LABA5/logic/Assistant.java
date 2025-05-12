package logic;

public class Assistant extends Person implements MedicalStaff {
    private String qualification;

    public Assistant(String name, int age, String qualification) {
        super(name, age, "Ассистент");
        this.qualification = qualification;
    }

    @Override
    public void interactWithPatient() {
        System.out.println("Ассистент " + name + " помогает пациенту");
    }

    @Override
    public void performDuties() {
        System.out.println("Ассистент " + name + " подготавливает инструменты");
    }

    public String getQualification() { return qualification; }
    public void setQualification(String qualification) { 
        this.qualification = qualification; 
    }

    @Override
    public String toString() {
        return String.format("Ассистент [ID: %d, Имя: %s, Квалификация: %s]", 
        personId, name, qualification);
    }
}