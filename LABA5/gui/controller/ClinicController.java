package gui.controller;

import logic.DentalClinic;
import logic.Person;
import utils.DatabaseManager;
import utils.Logger;
import utils.NoAvailableDentistException;

import javax.swing.JOptionPane;

import gui.view.ClinicView;
import gui.model.GUIModel;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import logic.Assistant;
import logic.DentalSpecialty;
import logic.Dentist;
import logic.Patient;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ClinicController {
    private DentalClinic clinic;
    private GUIModel guiModel;
    private ClinicView view;
    private DatabaseManager dbManager;
    
    public ClinicController(ClinicView view, GUIModel guiModel) {
        this.clinic = new DentalClinic();
        this.view = view;
        this.guiModel = guiModel;
        this.dbManager = new DatabaseManager(clinic);
    }
    
    // Метод сохранения данных
    public void saveData() {
        String[] options = {"База данных", "Файл", "Отмена"};
        int choice = JOptionPane.showOptionDialog(null,
            "Выберите способ сохранения данных:",
            "Сохранение данных",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
            
        if (choice == 0) {
            // Сохранение в базу данных
            dbManager.saveClinicData(clinic);
            guiModel.setDataChanged(false);
            view.updateResultText("Данные успешно сохранены в базу данных.");
            Logger.log("Сохранение данных в базу данных");
        } else if (choice == 1) {
            // Сохранение в файл
            try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream("clinic.ser"))) {
                oos.writeObject(clinic);
                guiModel.setDataChanged(false);
                view.updateResultText("Данные успешно сохранены в файл.");
                Logger.log("Сохранение данных в файл");
            } catch (IOException e) {
                view.updateResultText("Ошибка при сохранении в файл: " + e.getMessage());
                Logger.log("Ошибка при сохранении в файл", e.getMessage());
            }
        }
    }
    
    // Метод загрузки данных
    public void loadData() {
        String[] options = {"База данных", "Файл", "Отмена"};
        int choice = JOptionPane.showOptionDialog(null,
            "Выберите способ загрузки данных:",
            "Загрузка данных",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.QUESTION_MESSAGE,
            null,
            options,
            options[0]);
            
        if (choice == 0) {
            // Загрузка из базы данных
            dbManager.loadDataToClinic(clinic);
            guiModel.setDataChanged(false);
            view.updateResultText("Данные успешно загружены из базы данных.");
            Logger.log("Загрузка данных из базы данных");
        } else if (choice == 1) {
            // Загрузка из файла
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream("clinic.ser"))) {
                clinic = (DentalClinic) ois.readObject();
                guiModel.setDataChanged(false);
                view.updateResultText("Данные успешно загружены из файла.");
                Logger.log("Загрузка данных из файла");
            } catch (IOException | ClassNotFoundException e) {
                view.updateResultText("Ошибка при загрузке из файла: " + e.getMessage());
                Logger.log("Ошибка при загрузке из файла", e.getMessage());
            }
        }
    }
    
    // Обработка взаимодействия между стоматологами и пациентами
    private void processInteractions() {
        List<Patient> untreatedPatients = new ArrayList<>();
        List<Dentist> availableDentists = new ArrayList<>();
        List<Assistant> availableAssistants = new ArrayList<>();
        
        for (Person person : clinic.getAllPeople()) {
            if (person instanceof Patient) {
                Patient patient = (Patient) person;
                if (!patient.isBeingTreated()) {
                    untreatedPatients.add(patient);
                }
            } else if (person instanceof Dentist) {
                availableDentists.add((Dentist) person);
            } else if (person instanceof Assistant) {
                availableAssistants.add((Assistant) person);
            }
        }
        
        for (Patient patient : untreatedPatients) {
            try {
                boolean treated = false;
                
                //Поиск подходящего стоматолога
                for (Dentist dentist : availableDentists) {
                    if (DentalSpecialty.canTreat(dentist.getSpecialization(), patient.getDisease())) {
                        // Изменение статуса пациента в локальном списке
                        patient.setBeingTreated(true);
                        
                        // Обновляем статус в базе данных
                        String sql = "UPDATE patients SET is_being_treated = true WHERE id = ?";
                        try (PreparedStatement statement = dbManager.getConnection().prepareStatement(sql)) {
                            statement.setInt(1, patient.getPersonId());
                            statement.executeUpdate();
                        } catch (SQLException e) {
                            System.err.println("Ошибка при обновлении статуса пациента в базе данных: " + e.getMessage());
                        }
                        
                        // Информация о взаимодействии для отображения пользователю
                        String message = "Пациент " + patient.getName() + " с заболеванием " + 
                                       patient.getDisease() + " лечится стоматологом " + 
                                       dentist.getName() + " (" + dentist.getSpecialization() + ")";
                        System.out.println(message);
                        view.updateResultText(message);
                        
                        treated = true;
                        break;
                    }
                }
                
                if (!treated) {
                    throw new NoAvailableDentistException("Не найден подходящий стоматолог для пациента " + 
                                                        patient.getName() + " с заболеванием " + patient.getDisease());
                }
            } catch (NoAvailableDentistException e) {
                System.err.println(e.getMessage());
                view.updateResultText(e.getMessage());
                
                if (!availableAssistants.isEmpty()) {
                    Assistant assistant = availableAssistants.get(0);
                    
                    // Информация о взаимодействии для отображения пользователю
                    String message = "Ассистент " + assistant.getName() + 
                                   " помогает пациенту " + patient.getName() + 
                                   " с заболеванием " + patient.getDisease() + 
                                   " (нет подходящего стоматолога)";
                    System.out.println(message);
                    view.updateResultText(message);
                    
                    // Статус пациента остается "не лечится", так как ассистент не может вылечить
                } else {
                    String message = "Нет доступных ассистентов для помощи пациенту " + patient.getName();
                    System.err.println(message);
                    view.updateResultText(message);
                }
            }
        }
        
        guiModel.setDataChanged(true);
    }
    
    // Метод для взаимодействия со всеми
    public void interactAll() {
        processInteractions();
        Logger.log("Взаимодействие персонажей выполнено локально");
        view.showMessage("Интеракция выполнена.");
    }
    
    // Добавление персонажа
    public void addPerson(Person newPerson) {
        int databaseId = dbManager.addPerson(newPerson);
        
        if (databaseId != -1) {
            newPerson.setPersonId(databaseId);
            clinic.addPerson(newPerson);
            
            view.updateResultText("Добавлена персона: " + newPerson.toString());
            Logger.log("Добавление персонажа", newPerson.toString());
        } else {
            clinic.addPerson(newPerson);
            guiModel.setDataChanged(true);
            view.updateResultText("Персона добавлена локально, но не сохранена в базу данных: " + newPerson.toString());
            Logger.log("Ошибка при добавлении персонажа в базу данных", newPerson.toString());
        }
    }
    
    // Удаление персонажа по ID
    public void removePerson(int id) {
        Person person = clinic.findPerson(id);
        if(person != null) {
            clinic.removePerson(id);
            dbManager.removePerson(person);
            Logger.log("Удаление персонажа", person.toString());
            view.updateResultText("Удалена персона: " + person.toString());
            view.showMessage("Персона успешно удалена!");
        } else {
            view.updateResultText("Персона с ID " + id + " не найдена.");
            view.showMessage("Персона не найдена");
        }
    }
    
    // Поиск персонажа по ID
    public void findPerson(int id) {
        Person person = clinic.findPerson(id);
        if(person != null) {
            view.updateResultText("Найден персона: " + person.toString());
            view.showMessage("Найдено: " + person.toString());
        } else {
            view.updateResultText("Персона с ID " + id + " не найдена.");
            view.showMessage("Персона не найдена");
        }
    }
    
    // Завершение работы
    public void exitProcedure() {
        if(guiModel.isDataChanged()) {
            int choice = JOptionPane.showConfirmDialog(null, "Сохранить изменения перед выходом?",
                    "Сохранение данных", JOptionPane.YES_NO_CANCEL_OPTION);
            switch (choice) {
                case JOptionPane.YES_OPTION:
                    saveData();
                    view.updateResultText("Данные сохранены перед выходом.");
                    dbManager.closeConnection();
                    System.exit(0);
                    break;
                case JOptionPane.NO_OPTION:
                    view.updateResultText("Выход без сохранения.");
                    dbManager.closeConnection();
                    System.exit(0);
                    break;
                case JOptionPane.CANCEL_OPTION:
                default:
                    view.updateResultText("Выход отменён пользователем.");
                    break;
            }
        } else {
            view.updateResultText("Выход из программы.");
            dbManager.closeConnection();
            System.exit(0);
        }
    }
    
    // Геттер для доступа к логике
    public DentalClinic getClinic() {
        return clinic;
    }
} 