package logic;

public class Patient extends Person {
    private String disease;
    private boolean isBeingTreated;

    public Patient(String name, int age, String disease) {
        super(name, age, "Пациент");
        this.disease = disease;
        this.isBeingTreated = false;
    }

    @Override
    public void interactWithPatient() {
        System.out.println("Пациент " + name + " жалуется на " + disease);
    }

    public String getDisease() { 
        return disease;
    }
    
    public void setDisease(String disease) { 
        this.disease = disease; 
    }
    
    public boolean isBeingTreated() { 
        return isBeingTreated; 
    }
    
    public void setBeingTreated(boolean beingTreated) { 
        isBeingTreated = beingTreated; 
    }

    @Override
    public String toString() {
        return String.format("Пациент [ID: %d, Имя: %s, Заболевание: %s]", 
        personId, name, disease);
    }
}
