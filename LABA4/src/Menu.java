import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private static final String SAVE_FILE = "clinic.ser";
    private static Scanner scanner = new Scanner(System.in);
    private static DentalClinic clinic = new DentalClinic();

    public static void showMenu() {
        while (true) {
            System.out.println("\n=== Меню стоматологической клиники ===");
            System.out.println("1. Добавить нового человека");
            System.out.println("2. Удалить человека");
            System.out.println("3. Найти человека");
            System.out.println("4. Показать всех людей");
            System.out.println("5. Выполнить взаимодействие со всеми");
            System.out.println("6. Сохранить данные");
            System.out.println("7. Загрузить данные");
            System.out.println("0. Выход");

            int choice = getValidIntInput("Выберите действие: ", 0, 7);

            switch (choice) {
                case 1:
                    addNewPerson();
                    break;
                case 2:
                    removePerson();
                    break;
                case 3:
                    findPerson();
                    break;
                case 4:
                    clinic.displayAll();
                    break;
                case 5:
                    clinic.interactAll();
                    break;
                case 6:
                    saveToFile();
                    break;
                case 7:
                    loadFromFile();
                    break;
                case 0:
                    System.out.println("Программа завершена");
                    return;
            }
        }
    }

    private static void saveToFile() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(SAVE_FILE))) {
            oos.writeObject(clinic);
            System.out.println("Данные успешно сохранены");
        } catch (IOException e) {
            System.out.println("Ошибка при сохранении: " + e.getMessage());
        }
    }

    private static void loadFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(SAVE_FILE))) {
            clinic = (DentalClinic) ois.readObject();
            System.out.println("Данные успешно загружены");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Ошибка при загрузке: " + e.getMessage());
            clinic = new DentalClinic(); // Создаем новую клинику в случае ошибки
        }
    }

    private static void addNewPerson() {
        System.out.println("\nКого вы хотите добавить?");
        System.out.println("1. Стоматолог");
        System.out.println("2. Ассистент");
        System.out.println("3. Пациент");
    
        int type = getValidIntInput("Выберите тип: ", 1, 3);
        System.out.print("Введите имя: ");
        String name = scanner.nextLine();
        int age = getValidIntInput("Введите возраст: ", 1, 120);
    
        switch (type) {
            case 1:
                System.out.println("Выберите специализацию:");
                DentalSpecialty[] specialties = DentalSpecialty.values();
                for (int i = 0; i < specialties.length; i++) {
                    System.out.println((i + 1) + ". " + specialties[i].getTitle());
                }
                int specChoice = getValidIntInput("Ваш выбор: ", 1, specialties.length);
                clinic.addPerson(new Dentist(name, age, specialties[specChoice - 1].getTitle()));
                break;
            case 2:
                System.out.print("Введите квалификацию: ");
                String qual = scanner.nextLine();
                clinic.addPerson(new Assistant(name, age, qual));
                break;
            case 3:
                System.out.println("Выберите заболевание:");
                List<String> allDiseases = new ArrayList<>();
                for (DentalSpecialty specialty : DentalSpecialty.values()) {
                    for (String disease : specialty.getDiseases()) {
                        allDiseases.add(disease);
                    }
                }
                for (int i = 0; i < allDiseases.size(); i++) {
                    System.out.println((i + 1) + ". " + allDiseases.get(i));
                }
                int diseaseChoice = getValidIntInput("Ваш выбор: ", 1, allDiseases.size());
                clinic.addPerson(new Patient(name, age, allDiseases.get(diseaseChoice - 1)));
                break;
        }
        System.out.println("Человек успешно добавлен");
    }

    private static void removePerson() {
        clinic.displayAll();
        int id = getValidIntInput("Введите ID для удаления: ", 1, Integer.MAX_VALUE);
        clinic.removePerson(id);
        System.out.println("Операция завершена");
    }

    private static void findPerson() {
        int id = getValidIntInput("Введите ID для поиска: ", 1, Integer.MAX_VALUE);
        Person person = clinic.findPerson(id);
        if (person != null) {
            System.out.println("Найдено: " + person);
        } else {
            System.out.println("Человек не найден");
        }
    }

    private static int getValidIntInput(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.nextLine();
                int value = Integer.parseInt(input);
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Пожалуйста, введите число от " + min + " до " + max);
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите корректное число");
            }
        }
    }
}