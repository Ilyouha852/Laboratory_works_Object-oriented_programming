package logic;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import utils.NoAvailableDentistException;

public class DentalClinic implements Serializable {
    private List<Person> people;
    
    public DentalClinic() {
        this.people = new ArrayList<>();
    }

    public void addPerson(Person person) {
        people.add(person);
    }

    public void removePerson(int personId) {
        people.removeIf(person -> person.getPersonId() == personId);
    }

    public Person findPerson(int personId) {
        return people.stream()
                     .filter(person -> person.getPersonId() == personId)
                     .findFirst()
                     .orElse(null);
    }

    public void displayAll() {
        if (people.isEmpty()) {
            System.out.println("В клинике пока нет людей.");
        } else {
            for (Person person : people) {
                System.out.println(person);
            }
        }
    }

    public boolean hasPatients() {
        for (Person person : people) {
            if (person instanceof Patient) {
                return true;
            }
        }
        return false;
    }

    public boolean hasUntreatedPatients() {
        for (Person person : people) {
            if (person instanceof Patient) {
                Patient patient = (Patient) person;
                if (!patient.isBeingTreated()) {
                    return true;
                }
            }
        }
        return false;
    }

    private List<Patient> getUntreatedPatients() {
        List<Patient> untreatedPatients = new ArrayList<>();
        for (Person person : people) {
            if (person instanceof Patient) {
                Patient patient = (Patient) person;
                if (!patient.isBeingTreated()) {
                    untreatedPatients.add(patient);
                }
            }
        }
        return untreatedPatients;
    }

    public void interactAll() {
        if (people.isEmpty()) {
            System.out.println("В клинике пока нет людей для взаимодействия");
            return;
        }
    
        List<Patient> untreatedPatients = getUntreatedPatients();
    
        for (Person person : people) {
            if (person instanceof Dentist) {
                Dentist dentist = (Dentist) person;
                boolean foundPatient = false;
    
                for (Patient patient : untreatedPatients) {
                    if (!patient.isBeingTreated() && DentalSpecialty.canTreat(dentist.getSpecialization(), patient.getDisease())) {
                        System.out.println("\nРабота с пациентом " + patient.getName() + ":");
                        patient.interactWithPatient();
                        dentist.interactWithPatient();
                        dentist.performDuties();
                        patient.setBeingTreated(true);
                        foundPatient = true;
                        break;
                    }
                }
    
                if (!foundPatient) {
                    System.out.println("\nСтоматолог " + dentist.getName() + " (" + dentist.getSpecialization() +
                            ") ожидает пациентов по своей специализации");
                }
            } else if (person instanceof Assistant) {
                if (hasUntreatedPatients()) {
                    person.interactWithPatient();
                    ((Assistant) person).performDuties();
                } else {
                    System.out.println("\nАссистент " + person.getName() + " ожидает пациентов");
                }
            }
        }
    
        // Проверка, остались ли пациенты без лечения
        try {
            if (untreatedPatients.stream().anyMatch(patient -> !patient.isBeingTreated())) {
                throw new NoAvailableDentistException("Не найдено подходящего стоматолога для некоторых пациентов!");
            }
        } catch (NoAvailableDentistException e) {
            System.out.println("\nОшибка: " + e.getMessage());
        }
    }

    // Метод для сохранения данных в файл
    public void saveToFile(String fileName) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(this);
            System.out.println("Данные успешно сохранены.");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении данных: " + e.getMessage());
        }
    }

    // Метод для загрузки данных из файла
    public void loadFromFile(String fileName) {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            DentalClinic loadedClinic = (DentalClinic) ois.readObject();
            this.people = loadedClinic.people;
            System.out.println("Данные успешно загружены.");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка при загрузке данных: " + e.getMessage());
        }
    }

     // Геттер списка
    public List<Person> getAllPeople() {
        return new ArrayList<>(people);
    }    
}
