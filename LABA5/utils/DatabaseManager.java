package utils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import logic.Assistant;
import logic.DentalClinic;
import logic.Dentist;
import logic.Patient;
import logic.Person;

public class DatabaseManager {
    private static final String URL = "jdbc:postgresql://localhost:5432/dental_clinic";
    private static final String USER = "postgres";
    private static final String PASSWORD = "798551432005";
    
    private Connection connection;
    
    //Cоединение с базой данных
    public DatabaseManager() {
        try {
            Class.forName("org.postgresql.Driver");
            
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Соединение с базой данных установлено");
        } catch (ClassNotFoundException e) {
            System.err.println("Не найден драйвер PostgreSQL JDBC: " + e.getMessage());
        } catch (SQLException e) {
            System.err.println("Ошибка подключения к базе данных: " + e.getMessage());
        }
    }
    
    //Конструктор с автоматической загрузкой данных
    public DatabaseManager(DentalClinic clinic) {
        this();  // Вызываем основной конструктор для установки соединения
        
        if (connection != null) {
            loadDataToClinic(clinic);
            System.out.println("Данные успешно загружены из базы данных");
        }
    }
    
    //Закрытие соединения с базой данных
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Соединение с базой данных закрыто");
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при закрытии соединения: " + e.getMessage());
        }
    }
    
    //Добавление пациента в базу данных
    private int addPatient(Patient patient) {
        String sql = "INSERT INTO patients (name, age, disease, is_being_treated) VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, patient.getName());
            statement.setInt(2, patient.getAge());
            statement.setString(3, patient.getDisease());
            statement.setBoolean(4, patient.isBeingTreated());
            statement.executeUpdate();
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении пациента: " + e.getMessage());
        }
        return -1;
    }
    
    //Добавление стоматолога в базу данных
    private int addDentist(Dentist dentist) {
        String sql = "INSERT INTO dentists (name, age, specialization) VALUES (?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, dentist.getName());
            statement.setInt(2, dentist.getAge());
            statement.setString(3, dentist.getSpecialization());
            statement.executeUpdate();
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении стоматолога: " + e.getMessage());
        }
        return -1;
    }
    
    //Добавление ассистента в базу данных
    private int addAssistant(Assistant assistant) {
        String sql = "INSERT INTO assistants (name, age, qualification) VALUES (?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, assistant.getName());
            statement.setInt(2, assistant.getAge());
            statement.setString(3, assistant.getQualification());
            statement.executeUpdate();
            
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении ассистента: " + e.getMessage());
        }
        return -1;
    }
    
    //Добавление визита в базу данных
    public void addVisit(int patientId, Integer dentistId, Integer assistantId, String notes) {
        String sql = "INSERT INTO visits (patient_id, dentist_id, assistant_id, treatment_done, notes) VALUES (?, ?, ?, ?, ?)";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, patientId);
            
            if (dentistId != null) {
                statement.setInt(2, dentistId);
            } else {
                statement.setNull(2, Types.INTEGER);
            }
            
            if (assistantId != null) {
                statement.setInt(3, assistantId);
            } else {
                statement.setNull(3, Types.INTEGER);
            }
            
            statement.setBoolean(4, false);
            statement.setString(5, notes);
            
            statement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Ошибка при добавлении визита: " + e.getMessage());
        }
    }
    
    //Получение списка всех пациентов из базы данных
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients";
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String disease = resultSet.getString("disease");
                boolean isBeingTreated = resultSet.getBoolean("is_being_treated");
                
                Patient patient = new Patient(name, age, disease);
                if (isBeingTreated) {
                    patient.setBeingTreated(true);
                }
                
                patients.add(patient);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении списка пациентов: " + e.getMessage());
        }
        
        return patients;
    }
    
    //Получение списка всех стоматологов из базы данных
    public List<Dentist> getAllDentists() {
        List<Dentist> dentists = new ArrayList<>();
        String sql = "SELECT * FROM dentists";
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            
            while (resultSet.next()) {
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String specialization = resultSet.getString("specialization");
                
                Dentist dentist = new Dentist(name, age, specialization);
                dentists.add(dentist);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении списка стоматологов: " + e.getMessage());
        }
        
        return dentists;
    }
    
    //Получение списка всех ассистентов из базы данных
    public List<Assistant> getAllAssistants() {
        List<Assistant> assistants = new ArrayList<>();
        String sql = "SELECT * FROM assistants";
        
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            
            while (resultSet.next()) {
                // Получаем данные из результата запроса
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String qualification = resultSet.getString("qualification");
                
                Assistant assistant = new Assistant(name, age, qualification);
                assistants.add(assistant);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при получении списка ассистентов: " + e.getMessage());
        }
        
        return assistants;
    }
    

    public void loadDataToClinic(DentalClinic clinic) {
        // Очистка текущего списка
        List<Person> currentPeople = new ArrayList<>(clinic.getAllPeople());
        for (Person person : currentPeople) {
            clinic.removePerson(person.getPersonId());
        }
        
        // Загрузка пациентов
        String sql = "SELECT * FROM patients";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Patient patient = new Patient(
                    resultSet.getString("name"),
                    resultSet.getInt("age"),
                    resultSet.getString("disease")
                );
                patient.setPersonId(resultSet.getInt("id"));
                patient.setBeingTreated(resultSet.getBoolean("is_being_treated"));
                clinic.addPerson(patient);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при загрузке пациентов: " + e.getMessage());
        }
        
        // Загрузка стоматологов
        sql = "SELECT * FROM dentists";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Dentist dentist = new Dentist(
                    resultSet.getString("name"),
                    resultSet.getInt("age"),
                    resultSet.getString("specialization")
                );
                dentist.setPersonId(resultSet.getInt("id"));
                clinic.addPerson(dentist);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при загрузке стоматологов: " + e.getMessage());
        }
        
        // Загрузка ассистентов
        sql = "SELECT * FROM assistants";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                Assistant assistant = new Assistant(
                    resultSet.getString("name"),
                    resultSet.getInt("age"),
                    resultSet.getString("qualification")
                );
                assistant.setPersonId(resultSet.getInt("id"));
                clinic.addPerson(assistant);
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при загрузке ассистентов: " + e.getMessage());
        }
    }
    

    public void saveClinicData(DentalClinic clinic) {
        // Очистка текущих таблиц
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate("TRUNCATE TABLE visits CASCADE");
            statement.executeUpdate("TRUNCATE TABLE patients CASCADE");
            statement.executeUpdate("TRUNCATE TABLE dentists CASCADE");
            statement.executeUpdate("TRUNCATE TABLE assistants CASCADE");
        } catch (SQLException e) {
            System.err.println("Ошибка при очистке таблиц: " + e.getMessage());
            return;
        }
        
        // Сохранение всех людей
        for (Person person : clinic.getAllPeople()) {
            if (person instanceof Patient) {
                addPatient((Patient) person);
            } else if (person instanceof Dentist) {
                addDentist((Dentist) person);
            } else if (person instanceof Assistant) {
                addAssistant((Assistant) person);
            }
        }
    }
    
    //Получение ID из базы данных для объекта Person
    private int getDatabaseIdForPerson(Person person, String tableName) throws SQLException {
        String sql = "SELECT id FROM " + tableName + " WHERE name = ? AND age = ?";
        
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, person.getName());
            statement.setInt(2, person.getAge());
            
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("id");
                } else {
                    // Если персона не найдена в базе, добавляем её
                    if (tableName.equals("patients") && person instanceof Patient) {
                        addPatient((Patient) person);
                    } else if (tableName.equals("dentists") && person instanceof Dentist) {
                        addDentist((Dentist) person);
                    } else if (tableName.equals("assistants") && person instanceof Assistant) {
                        addAssistant((Assistant) person);
                    }
                    
                    // Повторно выполняем запрос для получения ID
                    try (PreparedStatement newStatement = connection.prepareStatement(sql)) {
                        newStatement.setString(1, person.getName());
                        newStatement.setInt(2, person.getAge());
                        
                        try (ResultSet newResultSet = newStatement.executeQuery()) {
                            if (newResultSet.next()) {
                                return newResultSet.getInt("id");
                            } else {
                                throw new SQLException("Не удалось добавить и получить ID для " + person.getName() + " в таблице " + tableName);
                            }
                        }
                    }
                }
            }
        }
    }
    
    //Добавление персоны в базу данных
    public int addPerson(Person person) {
        try {
            if (person instanceof Patient) {
                return addPatient((Patient) person);
            } else if (person instanceof Dentist) {
                return addDentist((Dentist) person);
            } else if (person instanceof Assistant) {
                return addAssistant((Assistant) person);
            } else {
                System.err.println("Неизвестный тип персоны");
                return -1;
            }
        } catch (Exception e) {
            System.err.println("Ошибка при добавлении персоны в базу данных: " + e.getMessage());
            return -1;
        }
    }
    
    //Удаление персоны из базы данных
    public void removePerson(Person person) {
        String tableName;
        if (person instanceof Patient) {
            tableName = "patients";
        } else if (person instanceof Dentist) {
            tableName = "dentists";
        } else if (person instanceof Assistant) {
            tableName = "assistants";
        } else {
            System.err.println("Неизвестный тип персоны");
            return;
        }
        
        try {
            int id = getDatabaseIdForPerson(person, tableName);
            String sql = "DELETE FROM " + tableName + " WHERE id = ?";
            
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Ошибка при удалении персоны: " + e.getMessage());
        }
    }
    
    //Получение соединения с базой данных
    public Connection getConnection() {
        return connection;
    }
} 