package gui.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import logic.DentalClinic;
import logic.Dentist;
import logic.Patient;
import logic.Person;
import logic.Assistant;

import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.Comparator;

public class ShowAllPeopleDialog extends JDialog {
    private DentalClinic clinic;
    private JTable table;
    private DefaultTableModel tableModel;

    public ShowAllPeopleDialog(JFrame parent, DentalClinic clinic) {
        super(parent, "Список людей", true);
        this.clinic = clinic;
        setupDialog();
    }

    private void setupDialog() {
        setSize(600, 400);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        JPanel sortPanel = new JPanel();
        JButton sortAscButton = new JButton("По возрастанию возраста");
        JButton sortDescButton = new JButton("По убыванию возраста");

        sortAscButton.addActionListener(e -> {
            List<Person> people = clinic.getAllPeople();
            people.sort(Comparator.comparingInt(Person::getAge));
            updateTable(people);
        });
        sortDescButton.addActionListener(e -> {
            List<Person> people = clinic.getAllPeople();
            people.sort((p1, p2) -> p2.getAge() - p1.getAge());
            updateTable(people);
        });

        sortPanel.add(sortAscButton);
        sortPanel.add(sortDescButton);
        add(sortPanel, BorderLayout.NORTH);

        String[] columnNames = {"ID", "Имя", "Возраст", "Роль", "Специализация", "Заболевание"};
        tableModel = new DefaultTableModel(columnNames, 0);
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JPopupMenu popupMenu = new JPopupMenu();
        JMenuItem deleteItem = new JMenuItem("Удалить");

        // Действие по удалению записи
        deleteItem.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                int personId = (int) tableModel.getValueAt(selectedRow, 0);
                clinic.removePerson(personId);
                tableModel.removeRow(selectedRow);
            }
        });

        popupMenu.add(deleteItem);

        // Отображение контекстного меню
        table.addMouseListener(new MouseAdapter() {
            private void showPopup(MouseEvent e) {
                int row = table.rowAtPoint(e.getPoint());
                if (row >= 0 && row < table.getRowCount()) {
                    table.setRowSelectionInterval(row, row);
                } else {
                    table.clearSelection();
                }
                if (e.isPopupTrigger() && e.getComponent() instanceof JTable) {
                    popupMenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
            @Override
            public void mousePressed(MouseEvent e) {
                showPopup(e);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                showPopup(e);
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);

        updateTable(clinic.getAllPeople());
    }

    private void updateTable(List<Person> people) {
        tableModel.setRowCount(0);
        for (Person p : people) {
            Object[] row = new Object[6];
            row[0] = p.getPersonId();
            row[1] = p.getName();
            row[2] = p.getAge();
            row[3] = p.getRole();
            if (p instanceof Dentist) {
                row[4] = ((Dentist) p).getSpecialization();
                row[5] = "-";
            } else if (p instanceof Patient) {
                row[4] = "-";
                row[5] = ((Patient) p).getDisease();
            } else if (p instanceof Assistant) {
                row[4] = ((Assistant) p).getQualification();
                row[5] = "-";
            }
            tableModel.addRow(row);
        }
    }
}
