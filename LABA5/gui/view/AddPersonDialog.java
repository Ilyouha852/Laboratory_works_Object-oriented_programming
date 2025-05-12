package gui.view;
import javax.swing.*;

import gui.controller.ClinicController;

import java.awt.*;

import logic.Person;
import logic.Dentist;
import logic.Assistant;
import logic.Patient;

public class AddPersonDialog extends JDialog {
    private ClinicController controller;
    private JComboBox<String> roleBox;
    
    public AddPersonDialog(JFrame parent, ClinicController controller) {
        super(parent, "Добавление персоны", true);
        this.controller = controller;
        setupFrame();        
    }
    
    private void setupFrame() {
        setSize(300, 300);
        setLayout(new GridLayout(6, 2, 10, 10));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        
        JLabel nameLabel = new JLabel("Имя:");
        JTextField nameField = new JTextField();
        JLabel ageLabel = new JLabel("Возраст:");
        JTextField ageField = new JTextField();
        JLabel roleLabel = new JLabel("Роль:");
        roleBox = new JComboBox<>(new String[]{"Стоматолог", "Ассистент", "Пациент"});
        JLabel extraLabel = new JLabel("Выберите специализацию");
        JComboBox<String> extraBox = new JComboBox<>();
        JButton addButton = new JButton("Добавить");
        JButton cancelButton = new JButton("Отмена");
        
        roleBox.addActionListener(e -> {
            String selectedRole = (String) roleBox.getSelectedItem();
            extraBox.removeAllItems();
            if (selectedRole.equals("Стоматолог")) {
                String[] dentalSpecialty = {"Терапевт", "Хирург", "Ортодонт", "Пародонтолог"};
                for (String specialty : dentalSpecialty) {
                    extraBox.addItem(specialty);
                }
                extraLabel.setText("Выберите специализацию");
            } else if (selectedRole.equals("Ассистент")) {
                String[] assistantSpecialty = {"Медбрат", "Медсестра"};
                for (String specialty : assistantSpecialty) {
                    extraBox.addItem(specialty);
                }
                extraLabel.setText("Выберите специализацию");
            } else if (selectedRole.equals("Пациент")) {
                String[] diseases = {"Кариес", "Пульпит", "Периодонтит", "Киста", "Перелом челюсти",
                        "Абсцесс", "Прикус", "Скученность зубов", "Диастема", "Гингивит", "Пародонтит", "Пародонтоз"};
                for (String disease : diseases) {
                    extraBox.addItem(disease);
                }
                extraLabel.setText("Выберите заболевание");
            }
        });
        
        add(nameLabel);
        add(nameField);
        add(ageLabel);
        add(ageField);
        add(roleLabel);
        add(roleBox);
        add(extraLabel);
        add(extraBox);
        add(addButton);
        add(cancelButton);
        
        addButton.addActionListener(e -> {
            try {
                Person newPerson = null;
                String name = nameField.getText();
                int age = Integer.parseInt(ageField.getText());
                String role = (String) roleBox.getSelectedItem();
                String extra = (String) extraBox.getSelectedItem();
                
                if (role.equals("Стоматолог")) {
                    newPerson = new Dentist(name, age, extra);
                } else if (role.equals("Ассистент")) {
                    newPerson = new Assistant(name, age, extra);
                } else if (role.equals("Пациент")) {
                    newPerson = new Patient(name, age, extra);
                }
                
                controller.addPerson(newPerson);
                JOptionPane.showMessageDialog(this, "Персона успешно добавлена!");
                dispose();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка: " + ex.getMessage());
            }
        });
        
        cancelButton.addActionListener(e -> {
            dispose();
        });
    }
}
