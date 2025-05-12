package gui.view;

import javax.swing.*;
import java.awt.*;

import gui.controller.ClinicController;
import gui.model.GUIModel;

public class ClinicView {
    private ClinicController controller;
    
    public GUIModel guiModel;
    
    private JFrame frame;
    private JTextArea resultTextArea;
    
    public ClinicView() {
        guiModel = new GUIModel();
        controller = new ClinicController(this, guiModel);
        setupGUI();
    }
    
    public void setController(ClinicController controller) {
        this.controller = controller;
    }
    
    public void updateResultText(String message) {
        appendResult(message);
    }

    public void appendResult(String message) {
        resultTextArea.append(message + "\n");
    }
    
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }
    
    // Построение интерфейса
    private void setupGUI() {
        frame = new JFrame("Dental Clinic Management");
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.setSize(600, 500);
        frame.setLayout(new BorderLayout());
        
        // Создание меню
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Файл");
        JMenuItem saveItem = new JMenuItem("Сохранить");
        JMenuItem loadItem = new JMenuItem("Загрузить");
        saveItem.addActionListener(e -> controller.saveData());
        loadItem.addActionListener(e -> controller.loadData());
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);
        
        JMenu infoMenu = new JMenu("Справка");
        JMenuItem guideItem = new JMenuItem("Открыть руководство");
        JMenuItem aboutItem = new JMenuItem("Информация");
        guideItem.addActionListener(e -> {
            showUserGuide();
            appendResult("Показано руководство пользователя.");
        });
        aboutItem.addActionListener(e -> {
            showAboutDialog();
            appendResult("Показана информация о программе.");
        });
        infoMenu.add(guideItem);
        infoMenu.add(aboutItem);
        
        menuBar.add(fileMenu);
        menuBar.add(infoMenu);
        frame.setJMenuBar(menuBar);
        
        // Главная панель с кнопками
        JPanel mainPanel = new JPanel(new GridLayout(8, 1, 10, 10));
        
        JButton addPersonButton = new JButton("Добавить человека");
        JButton removePersonButton = new JButton("Удалить человека");
        JButton findPersonButton = new JButton("Найти человека");
        JButton displayAllButton = new JButton("Показать всех");
        JButton interactAllButton = new JButton("Взаимодействовать со всеми");
        JButton saveButton = new JButton("Сохранить");
        JButton loadButton = new JButton("Загрузить");
        JButton exitButton = new JButton("Выход");
        
        mainPanel.add(addPersonButton);
        mainPanel.add(removePersonButton);
        mainPanel.add(findPersonButton);
        mainPanel.add(displayAllButton);
        mainPanel.add(interactAllButton);
        mainPanel.add(saveButton);
        mainPanel.add(loadButton);
        mainPanel.add(exitButton);
        
        frame.add(mainPanel, BorderLayout.CENTER);
        
        // Область для вывода результатов
        resultTextArea = new JTextArea(5, 30);
        resultTextArea.setEditable(false);
        JScrollPane resultScrollPane = new JScrollPane(resultTextArea);
        resultScrollPane.setBorder(BorderFactory.createTitledBorder("Результаты действий"));
        frame.add(resultScrollPane, BorderLayout.SOUTH);
        
        // Привязка обработчиков событий
        addPersonButton.addActionListener(e -> {
            new AddPersonDialog(frame, controller).setVisible(true);
        });
        removePersonButton.addActionListener(e -> new RemovePersonDialog(frame, controller).setVisible(true));
        findPersonButton.addActionListener(e -> new FindPersonDialog(frame, controller).setVisible(true));
        displayAllButton.addActionListener(e -> new ShowAllPeopleDialog(frame, controller.getClinic()).setVisible(true));
        interactAllButton.addActionListener(e -> controller.interactAll());
        saveButton.addActionListener(e -> controller.saveData());
        loadButton.addActionListener(e -> controller.loadData());
        exitButton.addActionListener(e -> controller.exitProcedure());
        
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                controller.exitProcedure();
            }
        });
        
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    
    private void showUserGuide() {
        JOptionPane.showMessageDialog(frame,
            "Руководство пользователя:\n1. Добавление персонажей осуществляется через кнопку 'Добавить человека', после нажатия которой появляется форма для заполнения информации о персонаже.\n2. Удаление персонажей осуществляется через кнопку 'Удалить человека', после нажатия которой появляется форма для ввода ID удаляемого персонажа.\n3. Показ всех персонажей и их характеристик осуществляется через кнопку 'Показать всех', после нажатия которой появляется список персонажей\nи возможность его сортировки по убыванию/возрастанию возраста персонажей.\n4. Сохранение данных персонажей осуществляется через кнопку 'Сохранить'.\n5. Загрузка данных персонажей из файла осуществляется через кнопку 'Загрузить'.\n6. Завершение работы программы осуществляется через кнопку 'Выход'\n\nТакже в главном меню присутствует строка-меню для быстрой работы с программой и получения информации о ней.",
            "Руководство", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void showAboutDialog() {
        JOptionPane.showMessageDialog(frame,
            "Данная программа реализована в ходе выполнения лабораторной работы 'Разработка приложения с оконным графическим пользовательским интерфейсом.\nАвтор: Иванов Илья\nУчебная группа: ИСТб-23-1",
            "О программе", JOptionPane.INFORMATION_MESSAGE);
    }
}
